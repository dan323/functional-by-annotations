package com.dan323.functional.annotation.compiler;

import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.annotation.algs.ISemigroup;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

public final class CompilerUtils {

    public static DeclaredType changeWildBy(Elements elementUtils, Types typeUtils, DeclaredType type, TypeMirror substitute, Class<?> interfaceClass) {
        var functorI = elementUtils.getTypeElement(interfaceClass.getTypeName());
        var wilderized = type;
        if (type.asElement().equals(functorI)) {
            var lst = type.getTypeArguments();
            wilderized = (DeclaredType) lst.get(0);
        } else {
            throw new IllegalArgumentException();
        }
        var funcList = wilderized.getTypeArguments().stream()
                .map((TypeMirror tm) -> {
                    if (tm.getKind().equals(TypeKind.WILDCARD)) {
                        return substitute;
                    } else {
                        return tm;
                    }
                }).toList().toArray(new TypeMirror[]{});
        return typeUtils.getDeclaredType((TypeElement) wilderized.asElement(), funcList);
    }

    public static List<DeclaredType> getAllMaximalFunctionalInterfaces(Elements elementUtils, TypeElement element) {
        var lst = element.getInterfaces().stream()
                .map((TypeMirror typeMirror) -> (DeclaredType) typeMirror)
                .filter(type -> CompilerUtils.isFunctional(elementUtils, type))
                .toList();
        return lst.stream().filter(type -> isMaximal(elementUtils, type, lst)).toList();
    }

    private static boolean isMaximal(Elements elements, DeclaredType type, List<DeclaredType> types) {
        var forest = FunctionalDependencyTree.getActualTree();
        return isMaximalRec(elements, forest, type, types);
    }

    private static boolean isMaximalRec(Elements elements, List<FunctionalDependencyTree> forest, DeclaredType type, List<DeclaredType> types) {
        if (forest.size() == 0) {
            return false;
        } else {
            var lst = new ArrayList<>(forest);
            for (var tree : forest) {
                var treeElement = elements.getTypeElement(tree.getNode().getTypeName());
                if (type.asElement().equals(treeElement)) {
                    return true;
                } else if (types.stream().map(DeclaredType::asElement).anyMatch(element -> element.equals(treeElement))) {
                    lst.remove(tree);
                }
            }
            return isMaximalRec(elements, lst.stream().flatMap(tr -> FunctionalDependencyTree.getNextLine(tr).stream()).toList(), type, types);
        }
    }

    private static boolean isFunctional(Elements elementUtils, DeclaredType type) {
        return isFunctor(elementUtils, type) || isApplicative(elementUtils, type) || isMonad(elementUtils, type);
    }

    private static boolean isFunctor(Elements elementUtils, DeclaredType type) {
        var functorI = elementUtils.getTypeElement(IFunctor.class.getTypeName());
        return type.asElement().equals(functorI);
    }

    private static boolean isMonad(Elements elementUtils, DeclaredType type) {
        var functorI = elementUtils.getTypeElement(IMonad.class.getTypeName());
        return type.asElement().equals(functorI);
    }

    private static boolean isApplicative(Elements elementUtils, DeclaredType type) {
        var functorI = elementUtils.getTypeElement(IApplicative.class.getTypeName());
        return type.asElement().equals(functorI);
    }

    private static boolean isSemigroup(Elements elements, DeclaredType type) {
        var functorI = elements.getTypeElement(ISemigroup.class.getTypeName());
        return type.asElement().equals(functorI);
    }
}
