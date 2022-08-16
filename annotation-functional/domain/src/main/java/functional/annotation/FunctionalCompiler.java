package functional.annotation;

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
import java.util.Map;
import java.util.Set;

public class FunctionalCompiler extends AbstractProcessor {

    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;
    private CompilerFactory compilerFactory;

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
            var elems = roundEnvironment.getElementsAnnotatedWithAny(Set.of(Functor.class, Applicative.class, Monad.class));
            for (var elem : elems) {
                if (elem instanceof TypeElement telem && !elem.getKind().equals(ElementKind.ANNOTATION_TYPE)) { // Valid kind
                    var ifaces = CompilerUtils.getAllMaximalFunctionalInterfaces(typeUtils, elementUtils, telem);
                    if (ifaces.map(iface -> Map.entry(iface, compilerFactory.from(iface, elementUtils, typeUtils, messager)))
                            .map(comp -> !comp.getValue().process(roundEnvironment, telem, comp.getKey())).orElse(true)) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            error("There was an exception launched: %s", e.getMessage());
            return false;
        }
    }

    private void error(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(Functor.class.getCanonicalName(), Applicative.class.getCanonicalName(), Monad.class.getCanonicalName());
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
