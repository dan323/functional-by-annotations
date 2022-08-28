package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.algs.IMonoid;

public final class MonoidUtil {

    public static <G extends IMonoid<A>, A> A unit(G monoid) {
        return FunctionalUtil.<G,A>monoidUnit(monoid)
                .orElseThrow(() -> new IllegalArgumentException("The monoid is not correctly implemented."));
    }

}
