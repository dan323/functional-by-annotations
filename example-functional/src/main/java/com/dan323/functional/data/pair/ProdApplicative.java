package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.Function;

@Applicative
public class ProdApplicative<M, N> extends ProdFunctor<M, N> implements IApplicative<PairTypeContructor<M, N, ?>> {

    public ProdApplicative(IApplicative<? extends M> fm, IApplicative<? extends N> fn) {
        super(fm, fn);
    }

    public <A> PairTypeContructor<M, N, A> pure(A a) {
        return new PairTypeContructor<>(ApplicativeUtil.pure((IApplicative<M>) mFunctor, a), ApplicativeUtil.pure((IApplicative<N>) nFunctor, a));
    }

    public <A, B> PairTypeContructor<M, N, B> fapply(PairTypeContructor<M, N, Function<A, B>> f, PairTypeContructor<M, N, A> c1) {
        return new PairTypeContructor<>(ApplicativeUtil.fapply((IApplicative<M>) mFunctor, c1.getFirst(), f.getFirst()), ApplicativeUtil.fapply((IApplicative<N>) nFunctor, c1.getSecond(), f.getSecond()));
    }
}
