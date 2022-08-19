package com.dan323.functional.data.function;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.BiFunction;
import java.util.function.Function;

@Monad
public final class FunctionFrom<A> implements IMonad<Function<A, ?>> {

    public static <B, C, A> Function<A, C> map(Function<A, B> base, Function<B, C> mapping) {
        return mapping.compose(base);
    }

    public static <B, A> Function<A, B> pure(B data) {
        return x -> data;
    }

    public static <B, C, A> Function<A, C> fapply(Function<A, Function<B, C>> ffun, Function<A, B> g) {
        return x -> ffun.apply(x).apply(g.apply(x));
    }

    public static <B, C, D, A> Function<A, D> liftA2(BiFunction<B, C, D> biFun, Function<A, B> fb, Function<A, C> fc) {
        return x -> biFun.apply(fb.apply(x), fc.apply(x));
    }

    public static <B, C, A> Function<A, C> flatMap(Function<B, Function<A, C>> ffun, Function<A, B> g) {
        return join(ffun.compose(g));
    }

    public static <B, A> Function<A, B> join(Function<A, Function<A, B>> ffun) {
        return x -> ffun.apply(x).apply(x);
    }
}
