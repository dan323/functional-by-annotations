package functional.annotation;

import functional.annotation.iface.IFunctor;

import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Annotation processor to verify all classes with {@link Functor} implement the correct {@code map} function
 *
 * @author daniel
 */
public class FunctorCompiler implements Compiler {

    private final Messager messager;
    private final Types typeUtils;
    private final Elements elementUtils;

    FunctorCompiler(Messager messager, Types types, Elements elements){
        this.messager = messager;
        this.typeUtils = types;
        this.elementUtils = elements;
    }

    @Override
    public boolean process(RoundEnvironment roundEnvironment, TypeElement element, DeclaredType iface) {
        if (!validateFunctor(element, iface)) {
            error("The annotated type %s does not satisfy the conditions", element.getQualifiedName());
            return false;
        } else {
            return true;
        }
    }

    private boolean validateFunctor(TypeElement element, DeclaredType iface) {
        // Verify that it is a public class
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error("The annotated type %s is not public", element.getQualifiedName());
            return false;
        }
        // Obtain the IFunctor interface it is implementing, or fail if it does not
        if (!iface.asElement().equals(elementUtils.getTypeElement(IFunctor.class.getTypeName()))) {
            error("The element %s is not implementing the interface Functor", element.getQualifiedName());
            return false;
        }
        boolean success = false;
        // Look for the public static method called map and verify its signature
        for (var elem : element.getEnclosedElements()) {
            if (elem.getKind().equals(ElementKind.METHOD) && elem.getModifiers().contains(Modifier.STATIC) && elem.getModifiers().contains(Modifier.PUBLIC) && elem.getSimpleName().toString().equals("map")) {
                if (checkIfMap((ExecutableElement) elem, element, iface)) {
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

    private boolean checkIfMap(ExecutableElement method, TypeElement type, DeclaredType iFace) {
        if (method.getParameters().size() == 2) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var input =  CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IFunctor.class);
            var returnTyp = CompilerUtils.changeWildBy(elementUtils, typeUtils,iFace, params.get(1), IFunctor.class);
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof DeclaredType param2 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(input.toString()) && returnType.toString().equals(returnTyp.toString())) {
                    var funcElem = elementUtils.getTypeElement(Function.class.getTypeName());
                    if (param2.asElement().equals(funcElem) && param2.getTypeArguments().equals(params.subList(0, 2))) {
                        return true;
                    } else {
                        warning("There is a map method, but for type %s is not a Function or the type arguments are not matching the rest of the signature", type.getQualifiedName().toString());
                    }
                } else {
                    warning("There is a map method, but the first input type and the return type should be %s", type.getQualifiedName());
                }
            } else {
                warning("There is a map method, but the signature is not parametrized");
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