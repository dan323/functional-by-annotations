package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public final class FiniteListFunctor implements IFunctor<FiniteList<?>> {

    public static <A,B> FiniteList<B> map(FiniteList<A> finiteList, Function<A,B> mapping){
        return finiteList.head().maybe(h -> FiniteList.cons(mapping.apply(h), map(finiteList.tail(), mapping)), FiniteList.nil());
    }
}
