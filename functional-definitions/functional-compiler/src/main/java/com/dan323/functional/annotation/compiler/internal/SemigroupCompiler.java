package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public final class SemigroupCompiler implements Compiler {
    private final Elements elementUtils;
    private final Messager messager;

    SemigroupCompiler(Messager messager, Elements elements) {
        this.messager = messager;
        this.elementUtils = elements;
    }

    @Override
    public void process(RoundEnvironment roundEnvironment, TypeElement element, DeclaredType iface) {
        validateSemigroup(element, iface);
    }

    private void validateSemigroup(TypeElement element, DeclaredType iface) {
        if (element.getAnnotation(Semigroup.class) == null) {
            error("The semigroup interface is not annotated as a semigroup");
        }
        // Verify that it is a public class
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error("The annotated type %s is not public", element.getQualifiedName());
        }
        // Obtain the ISemigroup interface it is implementing, or fail if it does not
        if (!iface.asElement().equals(elementUtils.getTypeElement(ISemigroup.class.getTypeName()))) {
            error("The element %s is not implementing the interface Semigroup", element.getQualifiedName());
        }
        boolean success = false;
        // Look for the public method called op and verify its signature
        for (var elem : element.getEnclosedElements()) {
            if (elem.getKind().equals(ElementKind.METHOD) && elem.getModifiers().contains(Modifier.PUBLIC)) {
                if (elem.getSimpleName().toString().equals(ISemigroup.OP_NAME) && checkOp((ExecutableElement) elem, iface)) {
                    success = true;
                } else {
                    warning(elem.getSimpleName().toString() + " is not an op method.");
                }
            } else {
                warning(elem.getSimpleName().toString() + " is not an op method.");
            }
        }
        if (!success) {
            error("The public function op was not found in %s", element.getQualifiedName());
        }
    }

    private boolean checkOp(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().size() == 2) {
            var params = iFace.getTypeArguments();
            if (method.getParameters().get(0).asType().toString().equals(params.get(0).toString()) && method.getParameters().get(1).asType().toString().equals(params.get(0).toString()) && method.getReturnType().toString().equals(params.get(0).toString())) {
                return true;
            } else {
                warning("There is a op method, but the input types and the return type are not as expected");
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
