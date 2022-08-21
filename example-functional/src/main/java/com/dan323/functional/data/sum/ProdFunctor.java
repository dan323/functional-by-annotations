package com.dan323.functional.data.sum;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.util.FunctorUtil;

import java.util.function.Function;

@Functor
public class ProdFunctor<M, N> implements IFunctor<Prod<M, N, ?>> {

    private final IFunctor<M> miFunctor;
    private final IFunctor<N> niFunctor;
    private final Class<M> mClass;
    private final Class<N> nClass;

    ProdFunctor(IFunctor<M> fm, IFunctor<N> fn, Class<M> mClass, Class<N> nClass) {
        this.miFunctor = fm;
        this.niFunctor = fn;
        this.nClass = nClass;
        this.mClass = mClass;
    }

    public <A, B> Prod<M, N, B> map(Prod<M, N, A> base, Function<A,B> function) {
        return new Prod<>(FunctorUtil.map(miFunctor, mClass, base.getFirst(), function), FunctorUtil.map(niFunctor, nClass, base.getSecond(), function));
    }
}
