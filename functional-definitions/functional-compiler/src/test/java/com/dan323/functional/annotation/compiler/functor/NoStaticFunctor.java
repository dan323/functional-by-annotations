package com.dan323.functional.annotation.compiler.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public class NoStaticFunctor implements IFunctor<List<?>> {

    public <A,B> List<B> map(List<A> lst, Function<A, B> mapping) {
        return List.of();
    }

}
