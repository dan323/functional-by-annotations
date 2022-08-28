package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.util.FunctorUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;
import java.util.stream.Stream;

@Functor
public class ProdFunctor<M, N> extends ProdFunctional<M,N> implements IFunctor<PairTypeContructor<M, N, ?>> {

    public ProdFunctor(IFunctor<M> mFunctor, IFunctor<N> nFunctor){
        super(mFunctor, nFunctor);
    }

    public <A, B> PairTypeContructor<M, N, B> map(PairTypeContructor<M, N, A> base, Function<A, B> function) {
        return new PairTypeContructor<>(FunctorUtil.map((IFunctor<M>) mFunctor, mClass, base.getFirst(), function), FunctorUtil.map((IFunctor<N>) nFunctor, nClass, base.getSecond(), function));
    }

    @Override
    public boolean isRightFunctional(Type type) {
        return type.getTypeName().contains("IFunctor") || type.getTypeName().contains("IApplicative") || type.getTypeName().contains("IMonad");
    }
}
