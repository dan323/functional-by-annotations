package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

public final class MonoidCompiler implements Compiler {

    private final Elements elementUtils;
    private final Messager messager;

    MonoidCompiler(Messager messager, Elements elements) {
        this.messager = messager;
        this.elementUtils = elements;
    }

    @Override
    public void process(RoundEnvironment roundEnvironment, TypeElement element, DeclaredType iface) {
        validateMonoid(element, iface);
    }

    private void validateMonoid(TypeElement element, DeclaredType iface) {
        if (element.getAnnotation(Monoid.class) == null) {
            error("The monoid interface is not annotated as a semigroup");
        }
        // Verify that it is a public class
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error("The annotated type %s is not public", element.getQualifiedName());
        }
        // Obtain the ISemigroup interface it is implementing, or fail if it does not
        if (!iface.asElement().equals(elementUtils.getTypeElement(IMonoid.class.getTypeName()))) {
            error("The element %s is not implementing the interface Semigroup", element.getQualifiedName());
        }
        boolean success = false;
        boolean successSemigroup = false;
        boolean successUnity = false;
        for (var elem : element.getEnclosedElements()) {
            if (elem.getKind().equals(ElementKind.METHOD) && elem.getModifiers().contains(Modifier.PUBLIC)) {
                if (elem.getSimpleName().toString().equals(IMonoid.OP_NAME) && checkOp((ExecutableElement) elem, iface)) {
                    successSemigroup = true;
                } else if (elem.getSimpleName().toString().equals(IMonoid.UNIT_NAME) && checkUnit((ExecutableElement) elem, iface)) {
                    successUnity = true;
                }
                if (successSemigroup && successUnity) {
                    success = true;
                    break;
                }
            }
        }
        if (!success) {
            error("The public function op or unit was not found in %s", element.getQualifiedName());
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

    private boolean checkUnit(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().isEmpty()) {
            var params = iFace.getTypeArguments();
            if (method.getReturnType().toString().equals(params.get(0).toString())) {
                return true;
            } else {
                warning("There is a unit method, but the return type are not as expected");
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
