package annotation;

import functional.annotation.Monad;
import functional.annotation.iface.IFunctor;

import java.util.List;
import java.util.function.Function;

@Monad
public class FunctorMonad implements IFunctor<List<?>> {

    public static <A, B> List<B> map(List<A> base, Function<A, B> map) {
        return List.of();
    }

}
