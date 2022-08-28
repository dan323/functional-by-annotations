package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;

/**
 * Implementation of no requirements
 *
 * @author daniel
 */
public class EmptyNecessaryMethods implements NecessaryMethods {
    @Override
    public NecessaryMethods process(ExecutableElement method) {
        return this;
    }

}
