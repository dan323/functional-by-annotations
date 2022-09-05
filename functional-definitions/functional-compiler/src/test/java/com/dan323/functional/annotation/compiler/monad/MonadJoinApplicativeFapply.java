package com.dan323.functional.annotation.compiler.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public class MonadJoinApplicativeFapply implements IMonad<List<?>> {

    public static <A> List<A> pure(A a) {
        return List.of();
    }

    public static <A, B> List<B> fapply(List<Function<A, B>> ffunction, List<A> base) {
        return ffunction.stream().flatMap(f -> base.stream().map(f)).toList();
    }

    public static <B> List<B> join(List<List<B>> base) {
        return List.of();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
