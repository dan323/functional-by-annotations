package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

import java.util.function.Function;

/**
 * Interface for infinite lists, lists implementing this interface should compute the elements lazily (only when queried)
 *
 * @param <A> type of elements in the list
 */
public abstract sealed class InfiniteList<A> implements List<A> permits Cons, Cycle, Generating, Generating.GeneratingMapped, Repeat, Zipped {

    @Override
    public abstract InfiniteList<A> tail();

    @Override
    public abstract <B> InfiniteList<B> map(Function<A, B> mapping);

    public abstract A getHead();

    @Override
    public Maybe<A> head() {
        return Maybe.of(getHead());
    }

    @Override
    public InfiniteList<A> cons(A head) {
        return new Cons<>(head, this);
    }

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
