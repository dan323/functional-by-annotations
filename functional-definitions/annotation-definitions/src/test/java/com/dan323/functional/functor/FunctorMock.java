package com.dan323.functional.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public final class FunctorMock implements IFunctor<List<?>> {

    private FunctorMock(){

    }

    public static final FunctorMock FUNCTOR = new FunctorMock();

    public static <T,R> List<R> map(List<T> base, Function<T,R> mapping){
        return base.stream().map(mapping).toList();
    }

}
