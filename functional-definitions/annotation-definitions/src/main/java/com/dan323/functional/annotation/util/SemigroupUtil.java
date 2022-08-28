package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.IApplicative;

/**
 * Access all applicative functions in an {@link ISemigroup} using reflexion
 *
 * @author daniel
 */
public final class SemigroupUtil {

    private SemigroupUtil() {
        throw new UnsupportedOperationException("This class has no instances");
    }

    public static <G extends ISemigroup<A>, A> A op(G semi, A a, A b) {
        return FunctionalUtil.semigroupOp(semi, a, b)
                .orElseThrow(() -> new IllegalArgumentException("The semigroup is not correctly implemented."));
    }

}
