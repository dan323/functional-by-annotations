package functional.annotation;

import functional.annotation.iface.IApplicative;
import functional.annotation.iface.IFunctor;
import functional.annotation.iface.IMonad;

import javax.annotation.processing.Messager;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public final class CompilerFactory {

    Compiler from(DeclaredType iface, Elements elements, Types types, Messager messager){
        if (iface.asElement().equals(elements.getTypeElement(IFunctor.class.getTypeName()))){
            return new FunctorCompiler(messager, types, elements);
        } else if (iface.asElement().equals(elements.getTypeElement(IApplicative.class.getTypeName()))){
            return new ApplicativeCompiler(messager, types, elements);
        } else if (iface.asElement().equals(elements.getTypeElement(IMonad.class.getTypeName()))){
            return new MonadCompiler(messager, types, elements);
        }
        throw new IllegalArgumentException("The interfaces does not represent an implemented functional");
    }
}
