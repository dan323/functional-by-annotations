package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public final class JListFunctor implements IFunctor<List<?>> {

    private JListFunctor(){
    }

    public static final JListFunctor FUNCTOR = new JListFunctor();
    public static <A,B> List<B> map(List<A> base, Function<A,B> mapping){
        return base.stream().map(mapping).toList();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
