package functional.data.optional;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.Optional;
import java.util.function.Function;

@Functor
public class JOptionalFunctor implements IFunctor<Optional<?>> {

    public static <A, B> Optional<B> map(Optional<A> base, Function<A, B> mapping) {
        return base.map(mapping);
    }
}
