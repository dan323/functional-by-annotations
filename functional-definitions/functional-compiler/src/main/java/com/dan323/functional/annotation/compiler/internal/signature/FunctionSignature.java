package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.ExecutableType;
import java.util.function.Function;

/**
 * Implementation of {@link NecessaryMethods} for just one method
 *
 * @author daniel
 */
public class FunctionSignature implements NecessaryMethods {

    private final Function<ExecutableType, Signature> validator;
    private final String name;

    public FunctionSignature(Function<ExecutableType, Signature> validator, String name) {
        this.validator = validator;
        this.name = name;
    }

    /**
     * Validates if the method processed is the one required or not
     *
     * @param method to be checked against our necessities
     * @return an empty necessity if the method checked, itself if the method did not check
     */
    @Override
    public NecessaryMethods process(ExecutableElement method) {
        if (validator.apply((ExecutableType) method.asType()).verifyMethod(method)) {
            return new EmptyNecessaryMethods();
        } else {
            return this;
        }
    }

    public String toString(){
        return name + " method";
    }
}
