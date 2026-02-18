package com.dan323.functional.data.optional;

import java.util.function.Function;

public sealed interface Maybe<A> permits Nothing, Just {

    <C> C maybe(Function<A, C> f, C constant);

    static <A> Maybe<A> of(A element) {
        return new Just<>(element);
    }

    static <A> Maybe<A> of() {
        return (Maybe<A>) Nothing.NOTHING;
    }

}
