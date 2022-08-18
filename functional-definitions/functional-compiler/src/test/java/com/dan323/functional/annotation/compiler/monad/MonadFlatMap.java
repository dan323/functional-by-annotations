package com.dan323.functional.annotation.compiler.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public class MonadFlatMap implements IMonad<List<?>> {

    public static <A,B> List<B> flatMap(Function<A,List<B>> map, List<A> base){
        return List.of();
    }

    public static <A> List<A> pure(A a){
        return List.of();
    }
}
