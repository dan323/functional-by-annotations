package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.algs.IMonoid;

/**
 * Access all applicative functions in an {@link IMonoid} using reflexion
 *
 * @author daniel
 */
public final class MonoidUtil {

    public static <A> A unit(IMonoid<? extends A> monoid) {
        return FunctionalUtil.<A>monoidUnit(monoid)
                .orElseThrow(() -> new IllegalArgumentException("The monoid is not correctly implemented."));
    }

}
