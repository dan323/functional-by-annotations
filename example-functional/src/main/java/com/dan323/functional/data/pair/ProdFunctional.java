package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.funcs.Functional;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public abstract class ProdFunctional<M,N> implements Functional<PairTypeContructor<M, N, ?>> {
    protected final Functional<? extends M> mFunctor;
    protected final Functional<? extends N> nFunctor;

    public ProdFunctional(Functional<? extends M> fm, Functional<? extends N> fn) {
        this.mFunctor = fm;
        this.nFunctor = fn;
    }

    @Override
    public Class<PairTypeContructor<M, N, ?>> getClassAtRuntime() {
        return (Class<PairTypeContructor<M, N, ?>>)(Class<?>) PairTypeContructor.class;
    }
}
