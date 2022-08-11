package functional.annotation.iface;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public final class FunctorUtils {

    public static <G extends IFunctor<F>,F, FA extends F, FB extends F, A, B> FB map(Class<? extends G> clazz, FA base, Function<A, B> function) {
        if (clazz.getAnnotation(functional.annotation.Functor.class) == null) {
            throw new IllegalArgumentException("The functor was not properly defined, as there is no annotation in the class.");
        }
        try {
            var mapMethod = clazz.getDeclaredMethod("map", clazz, Function.class);
            return (FB) mapMethod.invoke(null, base, function);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassCastException e) {
            throw new IllegalArgumentException("The functor was not properly defined", e);
        }
    }
}
