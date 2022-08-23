package com.dan323.functional.annotation.util;

import java.util.function.Function;

public final class MonadUtil {

    private MonadUtil(){
        throw new UnsupportedOperationException();
    }

    public static <G, F, FA extends F, FB extends F, A> FB flatMap(G monad, Class<F> fClass, Function<A, FB> mapping, FA fa) {
        return FunctionalUtil.monadFlatMap(monad, fClass, mapping, fa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

    public static <G, F, FFA extends F, FA extends F> FA join(G monad, Class<F> fClass, FFA ffa) {
        return FunctionalUtil.<G, F, FFA, FA>monadJoin(monad, fClass, ffa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }
}
