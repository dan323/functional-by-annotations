package com.dan323.functional.data.function;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

import java.util.function.UnaryOperator;

@Monoid
public final class EndoMonoid<A> implements IMonoid<UnaryOperator<A>> {

    private EndoMonoid() {
    }

    public UnaryOperator<A> op(UnaryOperator<A> f1, UnaryOperator<A> f2) {
        return (f1.compose(f2))::apply;
    }

    public UnaryOperator<A> unit() {
        return UnaryOperator.identity();
    }

    public static <A> EndoMonoid<A> getInstance() {
        return (EndoMonoid<A>) ENDO_MONOID;
    }

    private static final EndoMonoid<?> ENDO_MONOID = new EndoMonoid<>();
}
