package com.dan323.functional.annotation.compiler.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.compiler.applicative.ApplicativeMock;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public class MonadFlatMap extends ApplicativeMock implements IMonad<List<?>> {

    public MonadFlatMap(List<Integer> lst) {
        super(lst);
    }

    public static <A,B> List<B> flatMap(Function<A,List<B>> map, List<A> base){
        return List.of();
    }

}
