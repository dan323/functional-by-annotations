package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Abstraction of a signature of a method
 *
 * @author daniel
 */
public sealed class Signature permits Signature.InvalidSignature {
    private final List<TypeMirror> inputs;
    private final TypeMirror returnType;
    private final String name;

    Signature(List<TypeMirror> inputs, TypeMirror returnType, String name) {
        this.inputs = inputs;
        this.returnType = returnType;
        this.name = name;
    }

    /**
     * Verification if a method satifies the {@link Signature}
     *
     * @param method to check against
     * @return true iff the input method satisfies the signature
     */
    public boolean verifyMethod(ExecutableElement method) {
        return method.getReturnType().toString().equals(returnType.toString()) &&
                method.getParameters().size() == inputs.size() &&
                method.getSimpleName().toString().equals(name) &&
                verifyInputs(method);
    }

    private boolean verifyInputs(ExecutableElement method) {
        boolean b = true;
        for (int i = 0; b && i < inputs.size(); i++) {
            b = method.getParameters().get(i).asType().toString().equals(inputs.get(i).toString());
        }
        return b;
    }

    /**
     * A signature that always fails
     *
     * @return {@link InvalidSignature}
     */
    static Signature invalid() {
        return InvalidSignature.INVALID;
    }

    private static final class InvalidSignature extends Signature {

        private InvalidSignature() {
            super(List.of(), null, "");
        }

        private static final Signature INVALID = new InvalidSignature();

        @Override
        public boolean verifyMethod(ExecutableElement method) {
            return false;
        }
    }

}
