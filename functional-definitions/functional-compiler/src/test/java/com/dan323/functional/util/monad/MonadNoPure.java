package com.dan323.functional.util.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public final class MonadNoPure implements IMonad<List<?>> {

    private MonadNoPure(){

    }

    public static final MonadNoPure MONAD = new MonadNoPure();

    public static <A,B> List<B> flatMap(Function<A,List<B>> map, List<A> lst){
        return lst.stream().flatMap(x -> map.apply(x).stream()).toList();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
