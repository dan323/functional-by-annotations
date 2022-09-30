package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.Structure;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;

public final class CompilerUtils {

    public static DeclaredType changeWildBy(Types typeUtils, DeclaredType type, TypeMirror substitute) {
        var lst = type.getTypeArguments();
        var wilderized = (DeclaredType) lst.get(0);

        var funcList = wilderized.getTypeArguments().stream()
                .map((TypeMirror tm) -> {
                    if (tm.getKind().equals(TypeKind.WILDCARD)) {
                        return substitute;
                    } else if (tm.getKind().equals(TypeKind.DECLARED)) {
                        return changeWildBy(typeUtils, typeUtils.getDeclaredType((TypeElement) type.asElement(), tm), substitute);
                    } else {
                        return tm;
                    }
                }).toList().toArray(new TypeMirror[]{});
        return typeUtils.getDeclaredType((TypeElement) wilderized.asElement(), funcList);
    }

    public static List<DeclaredType> getAllMaximalFunctionalInterfaces(Elements elementUtils, Types types, TypeElement element) {
        var lst = element.getInterfaces().stream()
                .map((TypeMirror typeMirror) -> (DeclaredType) typeMirror)
                .filter(type -> CompilerUtils.isStrucutre(elementUtils, types, type))
                .toList();
        return lst.stream().filter(type -> isMaximal(elementUtils, type, lst)).toList();
    }

    private static boolean isMaximal(Elements elements, DeclaredType type, List<DeclaredType> types) {
        var forest = FunctionalDependencyTree.getActualTree();
        return isMaximalRec(elements, forest, type, types);
    }

    private static boolean isMaximalRec(Elements elements, List<FunctionalDependencyTree> forest, DeclaredType type, List<DeclaredType> types) {
        if (forest.isEmpty()) {
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

    private static boolean isStrucutre(Elements elementUtils, Types types, DeclaredType type) {
        return types.isAssignable(type, elementUtils.getTypeElement(Structure.class.getTypeName()).asType());
    }
}
