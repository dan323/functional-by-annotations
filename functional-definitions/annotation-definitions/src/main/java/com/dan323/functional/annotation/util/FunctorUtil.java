package com.dan323.functional.annotation.util;

import java.util.function.Function;

public final class FunctorUtil {

    private FunctorUtil(){
        throw new UnsupportedOperationException();
    }

    public static <G, F, FA extends F, FB extends F, A, B> FB map(G functor, Class<F> fClass, FA base, Function<A, B> function) {
        return FunctionalUtil.<G, F, FA, FB, A, B>functorMap(functor, fClass, base, function)
                .or(() -> FunctionalUtil.applicativeMap(functor, fClass, base, function))
                .or(() -> FunctionalUtil.monadMap(functor, fClass, base, function))
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

}
