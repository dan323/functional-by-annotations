package com.dan323.functional.annotation.compiler.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public class FunctorMock<A> implements IFunctor<List<?>> {

    private final List<A> lst;

    public FunctorMock(List<A> lst){
        this.lst = lst;
    }

    public static <T,R> List<R> map(List<T> base, Function<T,R> mapping){
        return base.stream().map(mapping).toList();
    }

    public List<A> toList(){
        return lst;
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
