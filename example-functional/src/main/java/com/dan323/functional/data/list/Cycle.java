package com.dan323.functional.data.list;

import java.util.function.Function;

public final class Cycle<A> extends InfiniteList<A> {

    private final FiniteList<A> cycle;

    Cycle(FiniteList<A> cycle) {
        if (cycle == null || cycle.length() == 0) {
            throw new IllegalArgumentException("The list to be cycled must not be null or empty.");
        }
        this.cycle = cycle;
    }

    @Override
    public A getHead() {
        return cycle.head().maybe(Function.identity(), null);
    }

    @Override
    public InfiniteList<A> tail() {
        var init = cycle.tail();
        return ListUtils.concat(init, this);
    }

    @Override
    public <B> Cycle<B> map(Function<A, B> mapping) {
        return new Cycle<>(cycle.map(mapping));
    }
}
