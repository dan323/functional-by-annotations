package com.dan323.functional.data.continuation;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface Continuation<A, R> extends Function<Function<A, R>, R> {

    static <R> R eval(Continuation<R,R> continuation){
        return continuation.apply(Function.identity());
    }

    static <A,B,R> Continuation<B,R> withCont(BiFunction<Function<B,R>,A,R> funs, Continuation<A,R> cont){
        return withCont2(t -> ((A x) -> funs.apply(t,x)), cont);
    }

    static <A,B,R> Continuation<B,R> withCont2(Function<Function<B,R>,Function<A,R>> funs, Continuation<A,R> cont){
        return (Continuation<B, R>) cont.compose(funs);
    }

}