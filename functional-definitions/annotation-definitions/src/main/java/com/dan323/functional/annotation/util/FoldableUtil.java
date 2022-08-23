package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.algs.IMonoid;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class FoldableUtil {

    private FoldableUtil() {
        throw new UnsupportedOperationException();
    }

    public static <G, F, FA extends F, A> A fold(G foldable, IMonoid<A> monoid, Class<F> fClass, FA a) {
        return FunctionalUtil.foldableFold(foldable, monoid, fClass, a)
                .orElseThrow(() -> new IllegalArgumentException("The foldable is not correctly implemented."));
    }

    public static <G,F,FA extends F, A, M> M foldMap(G foldable, IMonoid<M> monoid, Class<F> fClass, Function<A,M> function, FA base){
        return FunctionalUtil.foldableFoldMap(foldable, monoid, fClass, function, base)
                .orElseThrow(() -> new IllegalArgumentException("The foldable is not correctly implemented."));
    }

    public static <G,F,FA extends F, A, B> B foldr(G foldable, Class<F> fClass, BiFunction<A,B,B> function, B b, FA fa){
        return FunctionalUtil.foldableFoldr(foldable, fClass, function, b, fa)
                .orElseThrow(() -> new IllegalArgumentException("The foldable is not correctly implemented."));
    }

}
