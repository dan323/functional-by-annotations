package com.dan323.functional.annotation.util;

public final class SemigroupUtil {

    private SemigroupUtil() {
        throw new UnsupportedOperationException("This class has no instances");
    }

    public static <G, A> A op(G semi, Class<A> aClass, A a, A b) {
        return FunctionalUtils.semigroupOp(semi, aClass, a, b)
                .or(() -> FunctionalUtils.monoidOp(semi, aClass, a, b))
                .orElseThrow(() -> new IllegalArgumentException("The monoid is not correctly implemented."));
    }

}
