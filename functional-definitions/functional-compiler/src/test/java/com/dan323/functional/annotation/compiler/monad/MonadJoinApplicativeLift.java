package com.dan323.functional.annotation.compiler.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.BiFunction;

@Monad
public class MonadJoinApplicativeLift implements IMonad<List<?>> {

    public static <A> List<A> pure(A a) {
        return List.of();
    }

    public static <A, B, C> List<C> liftA2(BiFunction<A, B, C> function, List<A> base1, List<B> base2) {
        return base1.stream().flatMap(a -> base2.stream().map(b -> function.apply(a, b))).toList();
    }

    public static <B> List<B> join(List<List<B>> base) {
        return List.of();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
