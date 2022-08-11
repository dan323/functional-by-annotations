package functional.data.list;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public final class FiniteListFunctor implements IFunctor<FiniteList<?>> {

    public static <A,B> FiniteList<B> map(FiniteList<A> finiteList, Function<A,B> mapping){
        return finiteList.head().maybe(h -> FiniteList.cons(mapping.apply(h), map(finiteList.tail(), mapping)), FiniteList.nil());
    }
}
