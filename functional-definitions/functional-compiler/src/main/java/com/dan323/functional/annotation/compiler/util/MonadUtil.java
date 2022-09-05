package com.dan323.functional.annotation.compiler.util;

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

    public static <F, A> F flatMap(IMonad<? extends F> monad, Function<A, F> mapping, F fa) {
        return FunctionalUtil.monadFlatMap(monad, mapping, fa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

    public static <F> F join(IMonad<? extends F> monad, F ffa) {
        return FunctionalUtil.<F>monadJoin(monad, ffa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }
}
