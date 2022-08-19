package com.dan323.functional.annotation.compiler;

import com.dan323.functional.annotation.*;
import com.dan323.functional.annotation.compiler.internal.CompilerFactory;
import com.dan323.functional.annotation.compiler.internal.CompilerUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class FunctionalCompiler extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private CompilerFactory compilerFactory;

    private static final Set<Class<? extends Annotation>> annotations = Set.of(Functor.class, Applicative.class, Monad.class, Semigroup.class, Monoid.class);

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        compilerFactory = new CompilerFactory();
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            var elems = roundEnvironment.getElementsAnnotatedWithAny(annotations);
            for (var elem : elems) {
                if (elem instanceof TypeElement telem && !elem.getKind().equals(ElementKind.ANNOTATION_TYPE)) { // Valid kind
                    var ifaces = CompilerUtils.getAllMaximalFunctionalInterfaces(elementUtils, typeUtils, telem);
                    ifaces.stream().map(iface -> Map.entry(iface, compilerFactory.from(iface, elementUtils, typeUtils, messager)))
                            .forEach(comp -> comp.getValue().process(roundEnvironment, telem, comp.getKey()));
                }
            }
        } catch (Throwable e) {
            error("There was an exception or error launched: %s", e.getMessage());
        }
        return true;
    }

    private void error(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return annotations.stream().map(Class::getCanonicalName).collect(Collectors.toSet());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
