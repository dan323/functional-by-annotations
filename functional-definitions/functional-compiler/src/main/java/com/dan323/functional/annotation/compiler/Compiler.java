package com.dan323.functional.annotation.compiler;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public interface Compiler {

    void process(RoundEnvironment roundEnvironment, TypeElement element, DeclaredType iface);
}
