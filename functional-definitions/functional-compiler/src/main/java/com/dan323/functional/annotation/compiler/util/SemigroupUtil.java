package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.algs.ISemigroup;

/**
 * Access all applicative functions in an {@link ISemigroup} using reflexion
 *
 * @author daniel
 */
public final class SemigroupUtil {

    private SemigroupUtil() {
        throw new UnsupportedOperationException("This class has no instances");
    }

    public static <A> A op(ISemigroup<? extends A> semi, A a, A b) {
        return FunctionalUtil.semigroupOp(semi, a, b)
                .orElseThrow(() -> new IllegalArgumentException("The semigroup is not correctly implemented."));
    }

}
