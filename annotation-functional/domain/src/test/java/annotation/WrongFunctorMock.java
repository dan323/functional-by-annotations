package annotation;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public class WrongFunctorMock implements IFunctor<List<?>> {

    static <B, A> List<B> map(List<A> lst, Function<A, B> mapping) {
        return List.of();
    }
}
