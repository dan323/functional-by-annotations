package com.dan323.functional.data.optional;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public class MaybeFunctor implements IFunctor<Maybe<?>> {

    private MaybeFunctor(){

    }

    public static MaybeFunctor MAYBE_FUNCTOR = new MaybeFunctor();

    public static final MaybeFunctor MAYBE = new MaybeFunctor();

    public static <A,R> Maybe<R> map(Maybe<A> base, Function<A, R> f){
        return base.maybe(x -> Maybe.of(f.apply(x)), Maybe.of());
    }

}
