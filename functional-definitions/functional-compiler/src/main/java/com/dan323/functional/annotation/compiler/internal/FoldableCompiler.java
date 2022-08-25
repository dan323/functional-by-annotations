package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.funcs.IFoldable;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class FoldableCompiler implements Compiler {

    private final Messager messager;
    private final Types typeUtils;
    private final Elements elementUtils;

    FoldableCompiler(Messager messager, Types types, Elements elements) {
        this.messager = messager;
        this.typeUtils = types;
        this.elementUtils = elements;
    }

    @Override
    public void process(RoundEnvironment roundEnvironment, TypeElement element, DeclaredType iface) {
        validateFoldable(element, iface);
    }

    private void validateFoldable(TypeElement element, DeclaredType iface) {
        if (element.getAnnotation(Foldable.class) == null) {
            error("The foldable interface is not annotated as a foldable");
        }
        // Verify that it is a public class
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error("The annotated type %s is not public", element.getQualifiedName());
        }
        // Obtain the IFunctor interface it is implementing, or fail if it does not
        if (!iface.asElement().equals(elementUtils.getTypeElement(IFoldable.class.getTypeName()))) {
            error("The element %s is not implementing the interface Foldable", element.getQualifiedName());
        }
        boolean successFoldMap = false;
        boolean successFoldr = false;
        boolean success = false;
        // Look for the public method called map and verify its signature
        do {
            for (var elem : element.getEnclosedElements()) {
                if (elem.getKind().equals(ElementKind.METHOD) && elem.getModifiers().contains(Modifier.PUBLIC)) {
                    if (elem.getSimpleName().toString().equals(IFoldable.FOLD_MAP_NAME) && checkIfFoldMap((ExecutableElement) elem, element, iface)) {
                        successFoldMap = true;
                    } else if (elem.getSimpleName().toString().equals(IFoldable.FOLDR_NAME) && checkIfFoldr((ExecutableElement) elem, element, iface)) {
                        successFoldr = true;
                    }
                    if (successFoldMap || successFoldr) {
                        success = true;
                        break;
                    }
                }
            }
            if (element.getSuperclass().toString().equals("none")) {
                break;
            } else {
                element = (TypeElement) ((DeclaredType) element.getSuperclass()).asElement();
            }
        } while (!success && !element.toString().equals("java.lang.Object"));
        if (!success) {
            error("The public functions required to be a monad were not found in %s", element.getQualifiedName());
        }
    }

    private boolean checkIfFoldr(ExecutableElement method, TypeElement type, DeclaredType iFace) {
        if (method.getParameters().size() == 3) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var biFun = typeUtils.getDeclaredType(elementUtils.getTypeElement(BiFunction.class.getTypeName()), params.get(0), params.get(1), params.get(1));
            var foldableA = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IFoldable.class);
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof TypeVariable param2 && method.getParameters().get(2).asType() instanceof DeclaredType param3 && method.getReturnType() instanceof TypeVariable returnType) {
                if (param1.toString().equals(biFun.toString()) && param2.toString().equals(params.get(1).toString()) && param3.toString().equals(foldableA.toString()) && returnType.toString().equals(params.get(1).toString())) {
                    return true;
                } else {
                    warning("There is a foldr method, but the first input type and the return type should be %s", type.getQualifiedName());
                }
            } else {
                warning("There is a foldr method, but the signature is not parametrized");
            }
        }
        return false;
    }

    private boolean checkIfFoldMap(ExecutableElement method, TypeElement type, DeclaredType iFace) {
        if (method.getParameters().size() == 3) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var fun = typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), params.get(1));
            var foldable = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IFoldable.class);
            var iMonoid = typeUtils.getDeclaredType(elementUtils.getTypeElement(IMonoid.class.getTypeName()), params.get(1));
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof DeclaredType param2 && method.getParameters().get(2).asType() instanceof DeclaredType param3 && method.getReturnType() instanceof TypeVariable returnType) {
                if (param1.toString().equals(iMonoid.toString()) && param2.toString().equals(fun.toString()) && param3.toString().equals(foldable.toString()) && returnType.toString().equals(params.get(1).toString())) {
                    return true;
                } else {
                    warning("There is a foldMap method, but the first input type and the return type should be %s", type.getQualifiedName());
                }
            } else {
                warning("There is a foldMap method, but the signature is not parametrized");
            }
        }
        return false;
    }

    private void error(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }

    private void warning(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.WARNING, String.format(message, args));
    }
}
