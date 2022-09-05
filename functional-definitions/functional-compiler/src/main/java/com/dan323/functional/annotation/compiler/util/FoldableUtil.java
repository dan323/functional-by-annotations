package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.funcs.IFoldable;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Access all applicative functions in an {@link IFoldable} using reflexion
 *
 * @author daniel
 */
public final class FoldableUtil {

    private FoldableUtil() {
        throw new UnsupportedOperationException();
    }

    public static <F, A> A fold(IFoldable<? extends F> foldable, IMonoid<? extends A> monoid, F a) {
        return FunctionalUtil.foldableFold(foldable, monoid, a)
                .orElseThrow(() -> new IllegalArgumentException("The foldable is not correctly implemented."));
    }

    public static <F, A, M> M foldMap(IFoldable<? extends F> foldable, IMonoid<? extends M> monoid, Function<A,M> function, F base){
        return FunctionalUtil.foldableFoldMap(foldable, monoid, function, base)
                .orElseThrow(() -> new IllegalArgumentException("The foldable is not correctly implemented."));
    }

    public static <F, A, B> B foldr(IFoldable<? extends F> foldable, BiFunction<A,B,B> function, B b, F fa){
        return FunctionalUtil.foldableFoldr(foldable, function, b, fa)
                .orElseThrow(() -> new IllegalArgumentException("The foldable is not correctly implemented."));
    }

}
