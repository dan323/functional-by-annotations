package com.dan323.functional.annotation.compiler.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public class MonadJoinFunctor implements IMonad<List<?>> {

    public static <A> List<A> pure(A a){
        return List.of();
    }

    public static <A,B> List<B> map(List<A> base, Function<A,B> map){
        return List.of();
    }

    public static <A> List<A> join(List<List<A>> base){
        return List.of();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
