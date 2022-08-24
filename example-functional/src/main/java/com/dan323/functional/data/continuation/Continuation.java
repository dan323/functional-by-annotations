package com.dan323.functional.data.continuation;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.Function;

@Monad
@FunctionalInterface
public interface Continuation<A, R> extends Function<Function<A, R>, R>, IMonad<Continuation<?, R>> {

    static <R1, R2, R> Continuation<R2, R> map(Continuation<R1, R> base, Function<R1, R2> mapping) {
        return k -> base.apply(k.compose(mapping));
    }

    static <R1, R> Continuation<R1, R> pure(R1 r) {
        return k -> k.apply(r);
    }

    static <R1, R2, R> Continuation<R2, R> fapply(Continuation<Function<R1, R2>, R> f, Continuation<R1, R> c1) {
        return k -> f.apply(h -> c1.apply(k.compose(h)));
    }

    static <R1,R> Continuation<R1,R> join(Continuation<Continuation<R1,R>,R> cc){
        return k -> cc.apply(c1 -> c1.apply(k));
    }
}