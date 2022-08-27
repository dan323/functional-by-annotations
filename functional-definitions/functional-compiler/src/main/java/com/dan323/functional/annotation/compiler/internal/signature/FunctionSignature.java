package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import java.util.function.Function;

public class FunctionSignature implements NecessaryMethods {

    private final Function<ExecutableType, Signature> validator;

    public FunctionSignature(Function<ExecutableType, Signature> validator) {
        this.validator = validator;
    }

    @Override
    public NecessaryMethods process(ExecutableElement method) {
        if (validator.apply((ExecutableType) method.asType()).verifyMethod(method)) {
            return new EmptyNecessaryMethods();
        } else {
            return this;
        }
    }
}
