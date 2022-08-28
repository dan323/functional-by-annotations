package com.dan323.functional.annotation.util;

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

    public static <G extends IFunctor<FW>, FW extends F, F, FA extends F, FB extends F, A, B> FB map(G functor, Class<F> fClass, FA base, Function<A, B> function) {
        return FunctionalUtil.<G, FW, F, FA, FB, A, B>functorMap(functor, fClass, base, function)
                .or(() -> FunctionalUtil.applicativeMap(functor, fClass, base, function))
                .or(() -> FunctionalUtil.monadMap(functor, fClass, base, function))
                .orElseThrow(() -> new IllegalArgumentException("The functor is not correctly implemented."));
    }

    public static <G extends IFunctor<FW>, FW extends F, F, FA extends F, FB extends F, B> FB mapConst(G functor, Class<F> fClass, FA base, B constant) {
        return FunctionalUtil.<G, FW, F, FA, FB, B>functorMapConst(functor, fClass, base, constant)
                .orElseThrow(() -> new IllegalArgumentException("The functor is not correctly implemented."));
    }

}
