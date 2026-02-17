package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public class ProdFunctor<M, N> extends ProdFunctional<M,N> implements IFunctor<PairTypeConstructor<M, N, ?>> {

    public ProdFunctor(IFunctor<? extends M> mFunctor, IFunctor<? extends N> nFunctor){
        super(mFunctor, nFunctor);
    }

    public <A, B> PairTypeConstructor<M, N, B> map(PairTypeConstructor<M, N, A> base, Function<A, B> function) {
        return new PairTypeConstructor<>(FunctorUtil.map((IFunctor<M>) mFunctor, base.getFirst(), function), FunctorUtil.map((IFunctor<N>) nFunctor, base.getSecond(), function));
    }

}
