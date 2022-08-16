package functional.annotation;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;

public interface Compiler {

    boolean process(RoundEnvironment roundEnvironment, TypeElement element, DeclaredType iface);
}
