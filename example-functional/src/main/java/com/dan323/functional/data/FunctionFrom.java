package com.dan323.functional.data;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public final class FunctionFrom<A> implements IFunctor<Function<A, ?>> {

    public static <B, C, A> Function<A, C> map(Function<A, B> base, Function<B, C> mapping) {
        return mapping.compose(base);
    }
}
