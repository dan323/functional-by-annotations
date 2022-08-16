package annotation.functor;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
class NoPublicTypeFunctor implements IFunctor<List<?>> {

    public static <A, B> List<B> map(List<A> base, Function<A, B> map) {
        return List.of();
    }

}
