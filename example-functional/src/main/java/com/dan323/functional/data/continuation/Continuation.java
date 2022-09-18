package com.dan323.functional.data.continuation;

import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface Continuation<A, R> extends Function<Function<A, R>, R> {

    static <R> R eval(Continuation<R,R> continuation){
        return continuation.apply(Function.identity());
    }

}