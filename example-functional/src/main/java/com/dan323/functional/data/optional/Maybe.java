package com.dan323.functional.data.optional;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public sealed interface Maybe<A> permits Nothing, Just {

    <C> C maybe(Function<A, C> f, C constant);

    static <A> Maybe<A> of(A element) {
        return new Just<>(element);
    }

    static <A> Maybe<A> of() {
        return (Maybe<A>) Nothing.NOTHING;
    }

}
