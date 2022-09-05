package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

/**
 * Access all applicative functions in an {@link IFunctor} using reflexion
 *
 * @author daniel
 */
public final class FunctorUtil {

    private FunctorUtil() {
        throw new UnsupportedOperationException();
    }

    public static <F, A, B> F map(IFunctor<? extends F> functor, F base, Function<A, B> function) {
        return FunctionalUtil.functorMap(functor, base, function)
                .or(() -> FunctionalUtil.applicativeMap(functor, base, function))
                .or(() -> FunctionalUtil.monadMap(functor, base, function))
                .or(()-> FunctionalUtil.traversalMap(functor, base, function))
                .orElseThrow(() -> new IllegalArgumentException("The functor is not correctly implemented."));
    }

    public static <F, B> F mapConst(IFunctor<? extends F> functor, F base, B constant) {
        return FunctionalUtil.functorMapConst(functor, base, constant)
                .orElseThrow(() -> new IllegalArgumentException("The functor is not correctly implemented."));
    }

}
