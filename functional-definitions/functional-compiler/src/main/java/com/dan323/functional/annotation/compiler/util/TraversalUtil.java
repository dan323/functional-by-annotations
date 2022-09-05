package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;

import java.util.function.Function;

public final class TraversalUtil {

    private TraversalUtil(){
        throw new UnsupportedOperationException();
    }

    public static <K, F, A> K traverse(ITraversal<? extends F> traversal, IApplicative<? extends K> applicative, Function<A, ? extends K> mapping, F fa) {
        return FunctionalUtil.travesalTraverse(traversal, applicative, mapping, fa)
                .orElseThrow(() -> new IllegalArgumentException("The traversal is not correctly implemented."));
    }

    public static <K, F> K sequenceA(ITraversal<? extends F> traversal, IApplicative<? extends K> applicative, F fa) {
        return FunctionalUtil.traversalSequenceA(traversal, applicative, fa)
                .orElseThrow(() -> new IllegalArgumentException("The traversal is not correctly implemented."));
    }

}
