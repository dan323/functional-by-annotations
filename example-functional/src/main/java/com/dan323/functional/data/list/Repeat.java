package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Infinite list of one element repeating itself
 *
 * @param <A>
 */
final class Repeat<A> extends InfiniteList<A> {

    private final A element;

    Repeat(A a) {
        if (a == null) throw new IllegalArgumentException("The repeated element must not be null.");
        this.element = a;
    }

    @Override
    public Maybe<A> head() {
        return Maybe.of(element);
    }

    @Override
    public InfiniteList<A> tail() {
        return this;
    }

    @Override
    public <B> InfiniteList<B> map(Function<A, B> mapping) {
        return new Repeat<>(mapping.apply(element));
    }

    @Override
    public int hashCode() {
        return 7 * element.hashCode();
    }

    @Override
    public String toString() {
        return "[" + element + ", ...]";
    }
}
