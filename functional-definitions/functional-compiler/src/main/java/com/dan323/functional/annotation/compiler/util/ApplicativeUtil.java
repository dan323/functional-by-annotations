package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.BiFunction;

/**
 * Access all applicative functions in an {@link IApplicative} using reflexion
 *
 * @author daniel
 */
public final class ApplicativeUtil {

    public static final String NOT_CORRECTLY_IMPLEMENTED = "The applicative is not correctly implemented.";

    private ApplicativeUtil() {
        throw new UnsupportedOperationException();
    }

    public static <F, A> F pure(IApplicative<? extends F> applicative, A a) {
        return FunctionalUtil.<F, A>applicativePure(applicative, a)
                .orElseThrow(() -> new IllegalArgumentException(NOT_CORRECTLY_IMPLEMENTED));
    }

    public static <F> F fapply(IApplicative<? extends F> applicative, F base, F ff) {
        return FunctionalUtil.applicativeFapply(applicative, base, ff)
                .or(() -> FunctionalUtil.monadFapply(applicative, base, ff))
                .orElseThrow(() -> new IllegalArgumentException(NOT_CORRECTLY_IMPLEMENTED));
    }

    public static <F, A, B, C> F liftA2(IApplicative<? extends F> applicative, BiFunction<A, B, C> map, F fa, F fb) {
        return FunctionalUtil.applicativeLiftA2(applicative, map, fa, fb)
                .or(() -> FunctionalUtil.monadLiftA2(applicative, map, fa, fb))
                .orElseThrow(() -> new IllegalArgumentException(NOT_CORRECTLY_IMPLEMENTED));
    }

    public static <F> F keepLeft(IApplicative<? extends F> applicative, F left, F right) {
        return FunctionalUtil.applicativeKeepLeft(applicative, left, right)
                .orElseThrow(() -> new IllegalArgumentException(NOT_CORRECTLY_IMPLEMENTED));
    }

    public static <F> F keepRight(IApplicative<? extends F> applicative, F left, F right) {
        return FunctionalUtil.applicativeKeepRight(applicative, left, right)
                .orElseThrow(() -> new IllegalArgumentException(NOT_CORRECTLY_IMPLEMENTED));
    }

}