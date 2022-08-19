package com.dan323.functional.annotation.util;

public final class MonoidUtil {

    public static <G, A> A unit(Class<G> gClass, Class<A> aClass) {
        return FunctionalUtils.monoidUnit(gClass, aClass)
                .orElseThrow(() -> new IllegalArgumentException("The monoid is not correctly implemented."));
    }

}
