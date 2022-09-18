package com.dan323.functional.data.continuation;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.Function;

@Monad
public final class ContinuationMonad<R> implements IMonad<Continuation<?, R>> {

    private ContinuationMonad() {
    }

    public static <R> ContinuationMonad<R> getInstance() {
        return new ContinuationMonad<>();
    }

    public <R1, R2> Continuation<R2, R> map(Continuation<R1, R> base, Function<R1, R2> mapping) {
        return k -> base.apply(k.compose(mapping));
    }

    public <R1> Continuation<R1, R> pure(R1 r) {
        return k -> k.apply(r);
    }

    public <R1, R2> Continuation<R2, R> fapply(Continuation<Function<R1, R2>, R> f, Continuation<R1, R> c1) {
        return k -> f.apply(h -> c1.apply(k.compose(h)));
    }

    public <R1> Continuation<R1, R> join(Continuation<Continuation<R1, R>, R> cc) {
        return k -> cc.apply(c1 -> c1.apply(k));
    }

    @Override
    public Class<Continuation<?,R>> getClassAtRuntime() {
        return (Class<Continuation<?,R>>)(Class<?>) Continuation.class;
    }
}
