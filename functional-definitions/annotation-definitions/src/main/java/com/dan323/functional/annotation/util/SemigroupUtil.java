package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.util.FunctionalUtils;

public final class SemigroupUtil {

    private SemigroupUtil() {
        throw new UnsupportedOperationException("This class has no instances");
    }

    public static <G, A> A op(Class<G> gClass, Class<A> aClass, A a, A b) {
        return FunctionalUtils.semigroupOp(gClass, aClass, a, b)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

}
