package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.algs.ISemigroup;

public final class SemigroupUtil {

    private SemigroupUtil() {
        throw new UnsupportedOperationException("This class has no instances");
    }

    public static <G extends ISemigroup<A>, A> A op(G semi, A a, A b) {
        return FunctionalUtil.semigroupOp(semi, a, b)
                .or(() -> FunctionalUtil.monoidOp(semi, a, b))
                .orElseThrow(() -> new IllegalArgumentException("The monoid is not correctly implemented."));
    }

}
