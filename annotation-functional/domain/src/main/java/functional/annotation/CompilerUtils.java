package functional.annotation;

import functional.annotation.iface.IApplicative;
import functional.annotation.iface.IFunctor;
import functional.annotation.iface.IMonad;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Comparator;
import java.util.Optional;

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

    public static Optional<DeclaredType> getAllMaximalFunctionalInterfaces(Elements elementUtils, TypeElement element) {
        return element.getInterfaces().stream()
                .map((TypeMirror typeMirror) -> (DeclaredType) typeMirror)
                .filter(type -> CompilerUtils.isFunctional(elementUtils, type))
                .min(orderByImplication(elementUtils));
    }

    private static Comparator<DeclaredType> orderByImplication(Elements elements) {
        return (type, t1) -> {
            if (type == null) return t1 == null ? 0 : 1;
            if (type.equals(t1)) {
                return 0;
            } else if (isFunctor(elements, type)) {
                return -1;
            } else if (isFunctor(elements, t1)) {
                return 1;
            } else if (isApplicative(elements, type)) {
                return -1;
            } else if (isApplicative(elements, t1)) {
                return 1;
            } else {
                return -1;
            }
        };
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
}
