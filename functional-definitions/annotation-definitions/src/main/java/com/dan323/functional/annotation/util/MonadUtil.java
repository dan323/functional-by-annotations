package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.Function;

/**
 * Access all applicative functions in an {@link IMonad} using reflexion
 *
 * @author daniel
 */
public final class MonadUtil {

    private MonadUtil(){
        throw new UnsupportedOperationException();
    }

    public static <G extends IMonad<FW>, FW extends F, F, FA extends F, FB extends F, A> FB flatMap(G monad, Class<F> fClass, Function<A, FB> mapping, FA fa) {
        return FunctionalUtil.monadFlatMap(monad, fClass, mapping, fa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

    public static <G extends IMonad<FW>, FW extends F, F, FFA extends F, FA extends F> FA join(G monad, Class<F> fClass, FFA ffa) {
        return FunctionalUtil.<G, FW, F, FFA, FA>monadJoin(monad, fClass, ffa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }
}
