package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

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

public final class MonadCompiler implements Compiler {

    private final Messager messager;
    private final Types typeUtils;
    private final Elements elementUtils;

    MonadCompiler(Messager messager, Types types, Elements elements) {
        this.messager = messager;
        this.typeUtils = types;
        this.elementUtils = elements;
    }

    @Override
    public void process(RoundEnvironment roundEnvironment, TypeElement element, DeclaredType iface) {
        validateMonad(element, iface);
    }

    private void validateMonad(TypeElement element, DeclaredType iface) {
        if (element.getAnnotation(Monad.class) == null) {
            error("The monad interface is not annotated as a monad");
        }
        // Verify that it is a public class
        if (!element.getModifiers().contains(Modifier.PUBLIC)) {
            error("The annotated type %s is not public", element.getQualifiedName());
        }
        // Obtain the IMonad interface it is implementing, or fail if it does not
        if (!iface.asElement().equals(elementUtils.getTypeElement(IMonad.class.getTypeName()))) {
            error("The element %s is not implementing the interface Applicative", element.getQualifiedName());
        }
        boolean successFlatmap = false;
        boolean successJoin = false;
        boolean successPure = false;
        boolean successMap = false;
        boolean successFapply = false;
        boolean successLiftA2 = false;
        boolean successApplicative = false;
        boolean success = false;
        // Look for the public methods called pure and fapply and verify its signature
        do {
            for (var elem : element.getEnclosedElements()) {
                if (elem.getKind().equals(ElementKind.METHOD) && elem.getModifiers().contains(Modifier.PUBLIC)) {
                    if (elem.getSimpleName().toString().equals(IMonad.FLAT_MAP_NAME) && checkIfFlatmap((ExecutableElement) elem, iface)) {
                        successFlatmap = true;
                    } else if (elem.getSimpleName().toString().equals(IMonad.JOIN_NAME) && checkIfJoin((ExecutableElement) elem, iface)) {
                        successJoin = true;
                    } else if (elem.getSimpleName().toString().equals(IMonad.PURE_NAME) && checkIfPure((ExecutableElement) elem, iface)) {
                        successPure = true;
                    } else if (elem.getSimpleName().toString().equals(IMonad.MAP_NAME) && checkIfMap((ExecutableElement) elem, iface)) {
                        successMap = true;
                    } else if (elem.getSimpleName().toString().equals(IMonad.FAPPLY_NAME) && checkIfFapply((ExecutableElement) elem, iface)) {
                        successFapply = true;
                    } else if (elem.getSimpleName().toString().equals(IMonad.LIFT_A2_NAME) && checkIfLiftA2((ExecutableElement) elem, iface)) {
                        successLiftA2 = true;
                    }
                    if (successFapply || successLiftA2) {
                        successApplicative = true;
                    }
                    if (((successJoin && (successMap || successApplicative)) || successFlatmap) && successPure) {
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

    private boolean checkIfLiftA2(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().size() == 3) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var input1 = typeUtils.getDeclaredType(elementUtils.getTypeElement(BiFunction.class.getTypeName()), params.get(0), params.get(1), params.get(2));
            var input2 = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IMonad.class);
            var input3 = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(1), IMonad.class);
            var returnTyp = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(2), IMonad.class);
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof DeclaredType param2 && method.getParameters().get(2).asType() instanceof DeclaredType param3 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(input1.toString()) && param2.toString().equals(input2.toString()) && param3.toString().equals(input3.toString())) {
                    if (returnType.toString().equals(returnTyp.toString())) {
                        return true;
                    } else {
                        warning("There is a liftA2 method, but with the wrong return type");
                    }
                } else {
                    warning("There is a liftA2 method, but with the wrong input types");
                }
            } else {
                warning("There is a liftA2 method, but with the signature is not parametrized");

            }
        }
        return false;
    }

    private boolean checkIfFapply(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().size() == 2) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var input = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IMonad.class);
            var func = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), params.get(1)), IMonad.class);
            var returnTyp = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(1), IMonad.class);
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof DeclaredType param2 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(func.toString()) && param2.toString().equals(input.toString()) && returnType.toString().equals(returnTyp.toString())) {
                    return true;
                } else {
                    warning("There is a fapply method, but the input types and the return type are not as expected");
                }
            } else {
                warning("There is a fapply method, but the signature is not parametrized");
            }
        }
        return false;
    }

    private boolean checkIfMap(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().size() == 2) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var input = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IMonad.class);
            var returnTyp = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(1), IMonad.class);
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof DeclaredType param2 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(input.toString()) && returnType.toString().equals(returnTyp.toString())) {
                    var funcElem = typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), params.get(1));
                    if (param2.toString().equals(funcElem.toString())) {
                        return true;
                    } else {
                        warning("There is a map method, but for type is not a Function or the type arguments are not matching the rest of the signature");
                    }
                } else {
                    warning("There is a map method, but the first input type and the return type are not the correct signature.");
                }
            } else {
                warning("There is a map method, but the signature is not parametrized.");
            }
        }
        return false;
    }

    private boolean checkIfJoin(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().size() == 1) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var input = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IMonad.class), IMonad.class);
            var returnTyp = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IMonad.class);
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(input.toString()) && returnType.toString().equals(returnTyp.toString())) {
                    return true;
                } else {
                    warning("There is a join method, but the signature does not match.");
                }
            } else {
                warning("There is a join method, but the signature is not parametrized");
            }
        }
        return false;
    }

    private boolean checkIfFlatmap(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().size() == 2) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var returnTyp = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(1), IMonad.class);
            var input1 = typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), returnTyp);
            var input2 = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IMonad.class);
            if (method.getParameters().get(0).asType() instanceof DeclaredType param1 && method.getParameters().get(1).asType() instanceof DeclaredType param2 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(input1.toString()) && param2.toString().equals(input2.toString()) && returnType.toString().equals(returnTyp.toString())) {
                    return true;
                } else {
                    warning("There is a flatMap method, but the signature does not match.");
                }
            } else {
                warning("There is a flatMap method, but the signature is not parametrized");
            }
        }
        return false;
    }

    private boolean checkIfPure(ExecutableElement method, DeclaredType iFace) {
        if (method.getParameters().size() == 1) {
            var params = ((ExecutableType) method.asType()).getTypeVariables();
            var input = params.get(0);
            var returnTyp = CompilerUtils.changeWildBy(elementUtils, typeUtils, iFace, params.get(0), IMonad.class);
            if (method.getParameters().get(0).asType() instanceof TypeVariable param1 && method.getReturnType() instanceof DeclaredType returnType) {
                if (param1.toString().equals(input.toString()) && returnType.toString().equals(returnTyp.toString())) {
                    return true;
                } else {
                    warning("There is a pure method, but the input type and the return type should be %s %s", input.toString(), returnType.toString());
                }
            } else {
                warning("There is a pure method, but the signature is not parametrized");
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
