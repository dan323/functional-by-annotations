package com.dan323.functional.annotation.compiler.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public class EitherF<A,B> implements IFunctor<EitherF<A,?>> {

    public static <B,C,A> EitherF<A,C> map(EitherF<A,B> base, Function<B,C> map){
        return null;
    }

}
