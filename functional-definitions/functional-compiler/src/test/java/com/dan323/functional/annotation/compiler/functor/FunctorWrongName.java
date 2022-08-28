package com.dan323.functional.annotation.compiler.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public class FunctorWrongName<A> implements IFunctor<List<?>> {

    public static <T,R> List<R> mop(List<T> base, Function<T,R> mapping){
        return base.stream().map(mapping).toList();
    }

}