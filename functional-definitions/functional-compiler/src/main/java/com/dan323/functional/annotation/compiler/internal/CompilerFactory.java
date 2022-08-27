package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.*;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.compiler.internal.signature.StructureSignatures;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFoldable;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.funcs.IMonad;

import javax.annotation.processing.Messager;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public final class CompilerFactory {

    public Compiler<?> from(DeclaredType iface, Elements elements, Types types, Messager messager) {
        StructureSignatures signatures = new StructureSignatures(elements, types);
        if (iface.asElement().equals(elements.getTypeElement(IFunctor.class.getTypeName()))) {
            return new Compiler<>(signatures.functorSignatureChecker(iface), Functor.class, messager);
        } else if (iface.asElement().equals(elements.getTypeElement(IApplicative.class.getTypeName()))) {
            return new Compiler<>(signatures.applicativeSignatureChecker(iface), Applicative.class, messager);
        } else if (iface.asElement().equals(elements.getTypeElement(IMonad.class.getTypeName()))) {
            return new Compiler<>(signatures.monadSignatureChecker(iface), Monad.class, messager);
        } else if (iface.asElement().equals(elements.getTypeElement(ISemigroup.class.getTypeName()))) {
            return new Compiler<>(signatures.semigroupSignatureChecker(iface), Semigroup.class, messager);
        } else if (iface.asElement().equals(elements.getTypeElement(IMonoid.class.getTypeName()))) {
            return new Compiler<>(signatures.monoidSignatureChecker(iface), Monoid.class, messager);
        } else if ((iface.asElement().equals(elements.getTypeElement(IFoldable.class.getTypeName())))) {
            return new Compiler<>(signatures.foldableSignatureChecker(iface), Foldable.class, messager);
        }
        throw new IllegalArgumentException("The interfaces does not represent an implemented functional");
    }
}
