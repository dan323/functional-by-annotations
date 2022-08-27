package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;

public interface NecessaryMethods {

    NecessaryMethods process(ExecutableElement method);

}
