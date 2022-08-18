package com.dan323.functional.annotation.compiler.functor;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Monad
public class FunctorMonad implements IFunctor<List<?>> {

    public static <A, B> List<B> map(List<A> base, Function<A, B> map) {
        return List.of();
    }

}
