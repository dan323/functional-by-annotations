package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.BiFunction;

/**
 * Access all applicative functions in an {@link IApplicative} using reflexion
 *
 * @author daniel
 */
public final class ApplicativeUtil {

    private ApplicativeUtil() {
        throw new UnsupportedOperationException();
    }

    public static <G extends IApplicative<FW>, FW extends F, F, FA extends F, A> FA pure(G applicative, Class<F> fClass, A a) {
        return FunctionalUtil.<G, FW, F, FA, A>applicativePure(applicative, fClass, a)
                .orElseThrow(() -> new IllegalArgumentException("The applicative is not correctly implemented."));
    }

    public static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F, FF extends F> FB fapply(G applicative, Class<F> fClass, FA base, FF ff) {
        return FunctionalUtil.<G, FW, F, FA, FB, FF>applicativeFapply(applicative, fClass, base, ff)
                .or(() -> FunctionalUtil.monadFapply(applicative, fClass, base, ff))
                .orElseThrow(() -> new IllegalArgumentException("The applicative is not correctly implemented."));
    }

    public static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F, FC extends F, A, B, C> FC liftA2(G applicative, Class<F> fClass, BiFunction<A, B, C> map, FA fa, FB fb) {
        return FunctionalUtil.<G, FW, F, FA, FB, FC, A, B, C>applicativeLiftA2(applicative, fClass, map, fa, fb)
                .or(() -> FunctionalUtil.monadLiftA2(applicative, fClass, map, fa, fb))
                .orElseThrow(() -> new IllegalArgumentException("The applicative is not correctly implemented."));
    }

    // TODO allow this functions to be implemented
    public static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F> FA keepLeft(G applicative, Class<F> fClass, FA left, FB right) {
        return FunctionalUtil.applicativeKeepLeft(applicative, fClass, left, right)
                .orElseThrow(() -> new IllegalArgumentException("The applicative is not correctly implemented."));
    }

    public static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F> FB keepRight(G applicative, Class<F> fClass, FA left, FB right) {
        return FunctionalUtil.applicativeKeepRight(applicative, fClass, left, right)
                .orElseThrow(() -> new IllegalArgumentException("The applicative is not correctly implemented."));
    }

}