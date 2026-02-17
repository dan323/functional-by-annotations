package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.Function;

@Applicative
public class ProdApplicative<M, N> extends ProdFunctor<M, N> implements IApplicative<PairTypeConstructor<M, N, ?>> {

    public ProdApplicative(IApplicative<? extends M> fm, IApplicative<? extends N> fn) {
        super(fm, fn);
    }

    public <A> PairTypeConstructor<M, N, A> pure(A a) {
        return new PairTypeConstructor<>(ApplicativeUtil.pure((IApplicative<M>) mFunctor, a), ApplicativeUtil.pure((IApplicative<N>) nFunctor, a));
    }

    public <A, B> PairTypeConstructor<M, N, B> fapply(PairTypeConstructor<M, N, Function<A, B>> f, PairTypeConstructor<M, N, A> c1) {
        return new PairTypeConstructor<>(ApplicativeUtil.fapply((IApplicative<M>) mFunctor, c1.getFirst(), f.getFirst()), ApplicativeUtil.fapply((IApplicative<N>) nFunctor, c1.getSecond(), f.getSecond()));
    }
}
