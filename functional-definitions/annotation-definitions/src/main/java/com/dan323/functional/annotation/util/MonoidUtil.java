package com.dan323.functional.annotation.util;

public final class MonoidUtil {

    public static <G, A> A unit(G monoid) {
        return (A) FunctionalUtil.monoidUnit(monoid)
                .orElseThrow(() -> new IllegalArgumentException("The monoid is not correctly implemented."));
    }

}
