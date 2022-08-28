package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.util.ApplicativeUtil;

import java.lang.reflect.Type;
import java.util.function.Function;

@Applicative
public class ProdApplicative<M, N> extends ProdFunctor<M, N> implements IApplicative<PairTypeContructor<M, N, ?>> {

    public ProdApplicative(IApplicative<M> fm, IApplicative<N> fn) {
        super(fm, fn);
    }

    public <A> PairTypeContructor<M, N, A> pure(A a) {
        return new PairTypeContructor<>(ApplicativeUtil.pure((IApplicative<M>) mFunctor, mClass, a), ApplicativeUtil.pure((IApplicative<N>) nFunctor, nClass, a));
    }

    public <A, B> PairTypeContructor<M, N, B> fapply(PairTypeContructor<M, N, Function<A, B>> f, PairTypeContructor<M, N, A> c1) {
        return new PairTypeContructor<>(ApplicativeUtil.fapply((IApplicative<M>) mFunctor, mClass, c1.getFirst(), f.getFirst()), ApplicativeUtil.fapply((IApplicative<N>) nFunctor, nClass, c1.getSecond(), f.getSecond()));
    }

    @Override
    public boolean isRightFunctional(Type type) {
        return type.getTypeName().contains("IApplicative") || type.getTypeName().contains("IMonad");
    }
}
