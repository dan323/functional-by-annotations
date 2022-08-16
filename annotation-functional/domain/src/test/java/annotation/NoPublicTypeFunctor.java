package annotation;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
class NoPublicTypeFunctor implements IFunctor<List<?>> {

    public static <A, B> List<B> map(Function<A, B> map, List<A> base) {
        return List.of();
    }

}
