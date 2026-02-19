package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;

import java.util.function.Function;

@Traversal
public final class PairSecondMonad<A> implements ITraversal<Pair<A,?>> {

    private PairSecondMonad() {
    }

    private static final PairSecondMonad<?> PAIR_FIRST_MONAD = new PairSecondMonad<>();

    public static <A> PairSecondMonad<A> getInstance() {
        return (PairSecondMonad<A>) PAIR_FIRST_MONAD;
    }

    public <C, B> Pair<A,C> map(Pair<A,B> pair, Function<B, C> function) {
        return pair.mapSecond(function);
    }

    public <K,B> K traverse(IApplicative<K> applicative, Function<B, K> fun, Pair<A,B> lst) {
        var k = fun.apply(lst.getValue());
        return FunctorUtil.map(applicative, k, x -> new Pair<>(lst.getKey(),x));
    }

    @Override
    public Class<Pair<A,?>> getClassAtRuntime() {
        return (Class)Pair.class;
    }
}
