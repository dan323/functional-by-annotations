package functional.annotation.iface.util;

import functional.annotation.iface.util.FunctionalUtils;

import java.util.function.Function;

public final class FunctorUtil {

    private FunctorUtil(){
        throw new UnsupportedOperationException();
    }

    public static <G, F, FA extends F, FB extends F, A, B> FB map(Class<G> gClass, Class<F> fClass, FA base, Function<A, B> function) {
        return FunctionalUtils.<G, F, FA, FB, A, B>functorMap(gClass, fClass, base, function)
                .or(() -> FunctionalUtils.applicativeMap(gClass, fClass, base, function))
                .or(() -> FunctionalUtils.monadMap(gClass, fClass, base, function))
                .orElseThrow(() -> new IllegalArgumentException("The monad is not correctly implemented."));
    }

}
