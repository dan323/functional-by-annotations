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


    /*
    @Override
    <R> Optional<R> flatMap(Function<A, Monad<R, Optional<?>>> f);

    @Override
    <R> Optional<R> fapply(Applicative<Function<A, R>, Optional<?>> applicative);

    @Override
    default <Q> Optional<Q> pure(Q a) {
        return of(a);
    }

    @Override
    default <R> Optional<R> join(Monad<Monad<R, Optional<?>>, Optional<?>> monadMonad) {
        var optOpt = ((Optional<Optional<R>>) (Monad<?, ?>) monadMonad);
        return optOpt.maybe(Function.identity(), of());
    }*/
}
