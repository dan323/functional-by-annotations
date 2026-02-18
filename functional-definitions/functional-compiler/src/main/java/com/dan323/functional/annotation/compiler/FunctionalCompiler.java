package com.dan323.functional.annotation.compiler;

import com.dan323.functional.annotation.compiler.internal.InternalApi;
import com.dan323.functional.annotation.compiler.internal.CompilerFactory;
import com.dan323.functional.annotation.compiler.internal.CompilerUtils;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Annotation processor for functional structures.
 *<p/>
 * This is an internal processor discovered via SPI and should not be used directly.
 */
@InternalApi("Annotation processor - use via Java's annotation processing framework")
public final class FunctionalCompiler extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private CompilerFactory compilerFactory;

    private static final Set<Class<? extends Annotation>> annotations = CompilerFactory.getSupportedAnnotations();
    private static final java.util.Map<Class<? extends Annotation>, String> ANNOTATION_TO_INTERFACE = CompilerFactory.getAnnotationToInterfaceMap();

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
                    var directIfaces = CompilerUtils.getDirectFunctionalInterfaces(elementUtils, typeUtils, telem);
                    var allHierarchyIfaces = CompilerUtils.getAllFunctionalInterfacesFromHierarchy(elementUtils, typeUtils, telem);

                    // Check that for each direct annotation, the corresponding interface is implemented (directly or inherited)
                    checkDirectAnnotationsHaveInterfaces(telem, allHierarchyIfaces);

                    directIfaces.stream().map(iface -> compilerFactory.from(iface, elementUtils, typeUtils, messager))
                            .forEach(comp -> comp.process(telem));
                }
            }
        } catch (Throwable e) {
            error("There was an exception or error launched: %s", e.getMessage());
        }
        return true;
    }

    private void checkDirectAnnotationsHaveInterfaces(TypeElement element, List<DeclaredType> implementedInterfaces) {
        var directAnnotations = getDirectAnnotations(element);
        var interfaceNames = implementedInterfaces.stream()
                .map(iface -> iface.asElement().getSimpleName().toString())
                .collect(Collectors.toSet());

        for (var annotation : directAnnotations) {
            String expectedInterfaceName = ANNOTATION_TO_INTERFACE.get(annotation);
            if (expectedInterfaceName != null && !interfaceNames.contains(expectedInterfaceName)) {
                error("The class %s is annotated with @%s but does not implement %s",
                    element.getQualifiedName(),
                    annotation.getSimpleName(),
                    expectedInterfaceName);
            }
        }
    }

    private Set<Class<? extends Annotation>> getDirectAnnotations(TypeElement element) {
        var result = new java.util.HashSet<Class<? extends Annotation>>();

        // Check only direct annotations on this element
        element.getAnnotationMirrors().stream()
                .map(annotation -> annotation.getAnnotationType().toString())
                .forEach(annotationType -> annotations.stream()
                        .filter(ann -> ann.getCanonicalName().equals(annotationType))
                        .forEach(result::add));


        return result;
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
