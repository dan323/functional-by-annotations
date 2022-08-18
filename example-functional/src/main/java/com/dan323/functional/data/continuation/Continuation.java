package com.dan323.functional.data.continuation;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
@FunctionalInterface
public interface Continuation<A, R> extends Function<Function<A, R>, R>, IFunctor<Continuation<?,R>> {

    static <R1, R2, R> Continuation<R2, R> map(Continuation<R1, R> base, Function<R1, R2> mapping) {
        return k -> base.apply(k.compose(mapping));
    }
}

/*
public interface Continuation<A,R> extends Function<Function<A,R>,R>, Functor<A,Continuation<?,R>> {

    @Override
    default  <S> Continuation<S, R> pure(S r) {
        return f -> f.apply(r);
    }

    @Override
    default  <S> Continuation<S, R> join(Monad<Monad<S, Continuation<?, R>>, Continuation<?, R>> monadMonad) {
        return k -> ((Continuation<Continuation<S,R>,R>)(Monad<?,?>)monadMonad).apply(f -> f.apply(k));
    }

    @Override
    default <Q> Continuation<Q, R> fapply(Applicative<Function<A, Q>, Continuation<?, R>> ff){
        var fg = (Continuation<Function<A,Q>,R>) ff;
        return u -> fg.apply(q -> apply(u.compose(q)));
    }
}
*/