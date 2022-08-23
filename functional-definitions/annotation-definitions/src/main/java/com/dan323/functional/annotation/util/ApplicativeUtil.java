package com.dan323.functional.annotation.util;

import java.util.function.BiFunction;

public final class ApplicativeUtil {

    private ApplicativeUtil(){
        throw new UnsupportedOperationException();
    }

    public static <G, F, FA extends F, A> FA pure(G applicative, Class<F> fClass, A a) {
        return FunctionalUtil.<G, F, FA, A>applicativePure(applicative, fClass, a).orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

    public static <G, F, FA extends F, FB extends F, FF extends F> FB fapply(G applicative, Class<F> fClass, FA base, FF ff) {
        return FunctionalUtil.<G, F, FA, FB, FF>applicativeFapply(applicative, fClass, base, ff)
                .or(() -> FunctionalUtil.monadFapply(applicative, fClass, base, ff))
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

    public static <G, F, FA extends F, FB extends F, FC extends F, A, B, C> FC liftA2(G applicative, Class<F> fClass, BiFunction<A, B, C> map, FA fa, FB fb) {
        return FunctionalUtil.<G, F, FA, FB, FC, A, B, C>applicativeLiftA2(applicative, fClass, map, fa, fb)
                .or(() -> FunctionalUtil.monadLiftA2(applicative, fClass, map, fa, fb))
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

}