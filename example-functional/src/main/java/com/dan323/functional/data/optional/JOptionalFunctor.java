package com.dan323.functional.data.optional;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.Optional;
import java.util.function.Function;

@Functor
public class JOptionalFunctor implements IFunctor<Optional<?>> {

    public static <A, B> Optional<B> map(Optional<A> base, Function<A, B> mapping) {
        return base.map(mapping);
    }
}
