package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;

/**
 * Interface abstracting the idea of a list of methods required for an implementation
 *
 * @author daniel
 */
public interface NecessaryMethods {

    /**
     * Process the method in the {@link NecessaryMethods} to eliminate the necessity of this method
     *
     * @param method to be checked against our necessities
     * @return a new implementation of {@link NecessaryMethods} that does not require the method we processed
     */
    NecessaryMethods process(ExecutableElement method);

}
