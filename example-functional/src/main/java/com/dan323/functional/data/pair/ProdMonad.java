package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.compiler.util.MonadUtil;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.Function;

@Monad
public class ProdMonad<M, N> extends ProdApplicative<M, N> implements IMonad<PairTypeConstructor<M, N, ?>> {

    public ProdMonad(IMonad<M> fm, IMonad<N> fn) {
        super(fm, fn);
    }

    public <A, B> PairTypeConstructor<M, N, B> flatMap(Function<A, PairTypeConstructor<M, N, B>> f, PairTypeConstructor<M, N, A> base) {
        return new PairTypeConstructor<>(MonadUtil.flatMap((IMonad<M>) mFunctor, (A x) -> f.apply(x).getFirst(), base.getFirst()), MonadUtil.flatMap((IMonad<N>) nFunctor, (A x) -> f.apply(x).getSecond(), base.getSecond()));
    }
}
