package functional.annotation.iface.util;

import java.util.function.Function;

public final class MonadUtil {

    public static <G, F, FA extends F, FB extends F, A> FB flatMap(Class<G> gClass, Class<F> fClass, Function<A, FB> mapping, FA fa) {
        return FunctionalUtils.monadFlatMap(gClass, fClass, mapping, fa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

    public static <G, F, FFA extends F, FA extends F> FA join(Class<G> gClass, Class<F> fClass, FFA ffa) {
        return FunctionalUtils.<G, F, FFA, FA>monadJoin(gClass, fClass, ffa)
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }
}
