package com.dan323.functional.data.list;

import java.util.function.Function;

/**
 * Interface for infinite lists, lists implementing this interface should compute the elements lazily (only when queried)
 *
 * @param <A> type of elements in the list
 */
public sealed abstract class InfiniteList<A> implements List<A> permits Cons, Generating, Generating.GeneratingMapped, Repeat, Zipped {

    @Override
    abstract public InfiniteList<A> tail();

    @Override
    abstract public <B> InfiniteList<B> map(Function<A, B> mapping);

    /**
     * Infinite lists are incomparable in a finite amount of time
     *
     * @param obj element to be compared to
     *
     * @throws UnsupportedOperationException when called, since infinite lists cannot be compared in a finite amount of time
     */
    @Override
    public final boolean equals(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return 7 * super.hashCode();
    }
}
