package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.funcs.Functional;

public abstract class ProdFunctional<M,N> implements Functional<PairTypeConstructor<M, N, ?>> {
    protected final Functional<? extends M> mFunctor;
    protected final Functional<? extends N> nFunctor;

    public ProdFunctional(Functional<? extends M> fm, Functional<? extends N> fn) {
        this.mFunctor = fm;
        this.nFunctor = fn;
    }

    @Override
    public Class<PairTypeConstructor<M, N, ?>> getClassAtRuntime() {
        return (Class<PairTypeConstructor<M, N, ?>>)(Class<?>) PairTypeConstructor.class;
    }
}
