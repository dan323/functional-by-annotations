package com.dan323.functional.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public final class MonadMock implements IMonad<List<?>> {

    private MonadMock(){

    }

    public static final MonadMock MONAD = new MonadMock();

    public static <A,B> List<B> flatMap(Function<A,List<B>> f, List<A> base){
        return base.stream().flatMap(x -> f.apply(x).stream()).toList();
    }

    public static <A> List<A> pure(A a){
        return List.of(a);
    }

}
