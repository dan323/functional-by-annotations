package functional.annotation;

import functional.annotation.iface.IFunctor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class FunctorCompiler extends AbstractProcessor {
    private Elements elementUtils;
    private Messager messager;
    private Types typeUtils;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<>();
        annotataions.add(Functor.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        var elems = roundEnvironment.getElementsAnnotatedWith(Functor.class);
        for (var elem : elems) {
            if (elem instanceof TypeElement && !elem.getKind().equals(ElementKind.ANNOTATION_TYPE)) { // Valid kind
                if (!validateFunctor((TypeElement) elem)) {
                    error("The annotated type %s does not satisfy the conditions", ((TypeElement) elem).getQualifiedName());
                    return false;
                }
            } else {
                error("@Functor is not valid for %s", elem.getKind().toString());
                return false;
            }
        }
        //verifyImplementsInterface();
        return true;
    }

    private boolean validateFunctor(TypeElement element) {
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error("The annotated type %s is not public", element.getQualifiedName());
            return false;
        }
        var iFace = element.getInterfaces().stream().map((TypeMirror iface) -> ((DeclaredType) iface)).filter(iface -> isProperFunctor(element, iface)).findFirst().orElse(null);
        if (iFace == null) {
            error("The element %s is not implementing the interface Functor", element.getQualifiedName());
            return false;
        }
        boolean success = false;
        for (var elem : element.getEnclosedElements()) {
            if (elem.getKind().equals(ElementKind.METHOD) && elem.getModifiers().contains(Modifier.STATIC) && elem.getModifiers().contains(Modifier.PUBLIC)) {
                if (checkIfMap((ExecutableElement) elem, element, iFace)) {
                    success = true;
                    break;
                }
            }
        }
        if (!success) {
            error("The static public function map was not found in %s", element.getQualifiedName());
            return false;
        } else {
            return true;
        }
    }

    private boolean isProperFunctor(TypeElement element, DeclaredType type) {
        var functorI = elementUtils.getTypeElement(IFunctor.class.getTypeName());
        if (type.asElement().equals(functorI)) {
            var lst = type.getTypeArguments();
            var input = lst.get(0);
            var finalType = changeWildBy(type, input);
            return finalType.toString().equals(element.asType().toString());
        }
        return false;
    }

    private DeclaredType changeWildBy(DeclaredType type, TypeMirror substitute) {
        var functorI = elementUtils.getTypeElement(IFunctor.class.getTypeName());
        var wilderized = type;
        if (type.asElement().equals(functorI)) {
            var lst = type.getTypeArguments();
            wilderized = (DeclaredType) lst.get(1);
        } else {
            error("The interface is not an %s", IFunctor.class.getName());
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

    private boolean checkIfMap(ExecutableElement method, TypeElement type, DeclaredType iFace) {
        if (method.getSimpleName().toString().equals("map") && method.getParameters().size() == 2) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var input = changeWildBy(iFace, params.get(0));
            var returnTyp = changeWildBy(iFace, params.get(1));
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof DeclaredType param2 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(input.toString()) && returnType.toString().equals(returnTyp.toString())) {
                    var funcElem = elementUtils.getTypeElement(Function.class.getTypeName());
                    if (param2.asElement().equals(funcElem) && param2.getTypeArguments().equals(params.subList(0, 2))) {
                        return true;
                    } else {
                        error("The mapping for type %s is not a Function or the type arguments are not matching the rest of the signature", type.getQualifiedName().toString());
                    }
                } else {
                    error("The first input type and the return type should be %s", type.getQualifiedName());
                }
            } else {
                error("The signature is not parametrized");
            }
        }
        return false;
    }

    private void error(String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args));
    }
}
