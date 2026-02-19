package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;

import java.util.function.Function;

@Traversal
public final class PairFirstMonad<A> implements ITraversal<Pair<?,A>> {

    private PairFirstMonad() {
    }

    private static final PairFirstMonad<?> PAIR_FIRST_MONAD = new PairFirstMonad<>();

    public static <A> PairFirstMonad<A> getInstance() {
        return (PairFirstMonad<A>) PAIR_FIRST_MONAD;
    }

    public <C, B> Pair<C,A> map(Pair<B,A> pair, Function<B, C> function) {
        return pair.mapFirst(function);
    }

    public <K,B> K traverse(IApplicative<K> applicative, Function<B, K> fun, Pair<B,A> lst) {
        var k = fun.apply(lst.getKey());
        return FunctorUtil.map(applicative, k, x -> new Pair<>(x, lst.getValue()));
    }

    @Override
    public Class<Pair<?, A>> getClassAtRuntime() {
        return (Class)Pair.class;
    }
}
