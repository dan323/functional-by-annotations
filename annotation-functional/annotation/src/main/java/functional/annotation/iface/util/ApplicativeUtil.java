package functional.annotation.iface.util;

import java.util.function.BiFunction;

public final class ApplicativeUtil {

    public static <G, F, FA extends F, A> FA pure(Class<G> gclass, Class<F> fClass, A a) {
        return FunctionalUtils.<G, F, FA, A>applicativePure(gclass, fClass, a).orElseThrow(() -> new IllegalArgumentException("The monad is not well defined"));
    }

    public static <G, F, FA extends F, FB extends F, FF extends F> FB fapply(Class<G> gClass, Class<F> fClass, FA base, FF ff) {
        return FunctionalUtils.<G, F, FA, FB, FF>applicativeFapply(gClass, fClass, base, ff)
                .or(() -> FunctionalUtils.monadFapply(gClass, fClass, base, ff))
                .orElseThrow(() -> new IllegalArgumentException("The monad is not well defined"));
    }

    public static <G, F, FA extends F, FB extends F, FC extends F, A, B, C> FC liftA2(Class<G> gClass, Class<F> fClass, BiFunction<A, B, C> map, FA fa, FB fb) {
        return FunctionalUtils.<G, F, FA, FB, FC, A, B, C>applicativeLiftA2(gClass, fClass, map, fa, fb)
                .or(() -> FunctionalUtils.monadLiftA2(gClass, fClass, map, fa, fb))
                .orElseThrow(() -> new IllegalArgumentException("The monad is not well defined"));

    }

}