package com.dan323.functional.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public final class FunctorMock<A> implements IFunctor<FunctorMock<?>> {

    private final List<A> lst;

    public FunctorMock(List<A> lst){
        this.lst = lst;
    }

    public static <T,R> FunctorMock<R> map(FunctorMock<T> base, Function<T,R> mapping){
        return new FunctorMock<>(base.lst.stream().map(mapping).toList());
    }

    public List<A> toList(){
        return lst;
    }
}
