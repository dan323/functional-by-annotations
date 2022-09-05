package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.compiler.internal.signature.EmptyNecessaryMethods;
import com.dan323.functional.annotation.compiler.internal.signature.NecessaryMethods;

import javax.annotation.processing.Messager;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.function.UnaryOperator;

public class Compiler<F extends Annotation> {

    private final NecessaryMethods necessaryMethods;
    private final Class<F> annotation;
    private final Messager messager;

    Compiler(NecessaryMethods necessaryMethods, Class<F> fClass, Messager messager) {
        this.necessaryMethods = necessaryMethods;
        this.annotation = fClass;
        this.messager = messager;
    }

    /**
     * Check if the input has all the necessary methods for all the annotations it has
     *
     * @param element type to check at compile time
     */
    public void process(TypeElement element) {
        TypeElement originalElement = element;
        if (element.getAnnotation(annotation) == null) {
            error("The functor interface is not annotated as a functor");
        }
        // Verify that it is a public class
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error("The annotated type %s is not public", originalElement.getQualifiedName());
        }
        boolean success = false;
        // Look for the public method called map and verify its signature
        var necessaryMethodsLoop = necessaryMethods;
        do {
            necessaryMethodsLoop = element.getEnclosedElements().stream()
                    .filter(element1 -> element1.getKind().equals(ElementKind.METHOD) && element1.getModifiers().contains(Modifier.PUBLIC))
                    .map(element1 -> (ExecutableElement) element1)
                    .map(element1 -> (UnaryOperator<NecessaryMethods>) ((NecessaryMethods nec) -> nec.process(element1)))
                    .reduce(necessaryMethodsLoop,
                            (nec, unary) -> unary.apply(nec),
                            (nec1, nec2) -> {
                                throw new UnsupportedOperationException("This operation is not suppoerted");
                            });
            success = necessaryMethodsLoop instanceof EmptyNecessaryMethods;
            if (element.getSuperclass().toString().equals("none")) {
                break;
            } else {
                element = (TypeElement) ((DeclaredType) element.getSuperclass()).asElement();
            }
        } while (!success && !element.toString().equals("java.lang.Object"));
        if (!success) {
            error("The public functions needed were not found in %s", originalElement.getQualifiedName());
        }
    }

    private void error(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }
}
