package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.funcs.IMonad;

import javax.annotation.processing.Messager;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public final class CompilerFactory {

    public Compiler from(DeclaredType iface, Elements elements, Types types, Messager messager) {
        if (iface.asElement().equals(elements.getTypeElement(IFunctor.class.getTypeName()))) {
            return new FunctorCompiler(messager, types, elements);
        } else if (iface.asElement().equals(elements.getTypeElement(IApplicative.class.getTypeName()))) {
            return new ApplicativeCompiler(messager, types, elements);
        } else if (iface.asElement().equals(elements.getTypeElement(IMonad.class.getTypeName()))) {
            return new MonadCompiler(messager, types, elements);
        } else if (iface.asElement().equals(elements.getTypeElement(ISemigroup.class.getTypeName()))) {
            return new SemigroupCompiler(messager, elements);
        } else if (iface.asElement().equals(elements.getTypeElement(IMonoid.class.getTypeName()))) {
            return new MonoidCompiler(messager, elements);
        }
        throw new IllegalArgumentException("The interfaces does not represent an implemented functional");
    }
}