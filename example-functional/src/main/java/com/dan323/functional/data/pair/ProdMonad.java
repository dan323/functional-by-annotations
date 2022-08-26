package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.annotation.util.MonadUtil;

import java.lang.reflect.Type;
import java.util.function.Function;

@Monad
public class ProdMonad<M, N> extends ProdApplicative<M, N> implements IMonad<PairTypeContructor<M, N, ?>> {

    public ProdMonad(IMonad<M> fm, IMonad<N> fn) {
        super(fm, fn);
    }

    public <A, B> PairTypeContructor<M, N, B> flatMap(Function<A, PairTypeContructor<M, N, B>> f, PairTypeContructor<M, N, A> base) {
        return new PairTypeContructor<>(MonadUtil.flatMap(mFunctor, mClass, (A x) -> f.apply(x).getFirst(), base.getFirst()), MonadUtil.flatMap(nFunctor, nClass, (A x) -> f.apply(x).getSecond(), base.getSecond()));
    }

    @Override
    public boolean isRightFunctional(Type type) {
        return type.getTypeName().contains("IMonad");
    }
}
