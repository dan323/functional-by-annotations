package com.dan323.functional.data.function;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

import java.util.function.Function;

@Monoid
public final class EndoMonoid<A> implements IMonoid<Function<A, A>> {

    private EndoMonoid(){}

    public Function<A, A> op(Function<A, A> f1, Function<A, A> f2) {
        return f1.compose(f2);
    }

    public Function<A, A> unit() {
        return Function.identity();
    }

    public static <A> EndoMonoid<A> getInstance(){
        return (EndoMonoid<A>) ENDO_MONOID;
    }

    private static final EndoMonoid<?> ENDO_MONOID = new EndoMonoid<>();
}
