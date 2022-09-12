package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

/**
 * Empty list
 *
 * @param <A>
 */
final class Nil<A> implements FiniteList<A> {

    private Nil() {
    }

    @Override
    public int length() {
        return 0;
    }

    static Nil<?> NIL = new Nil<>();

    @Override
    public Maybe<A> head() {
        return Maybe.of();
    }

    @Override
    public FiniteList<A> tail() {
        return this;
    }

    @Override
    public String toString() {
        return "[]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj == NIL;
    }

    @Override
    public int hashCode() {
        return 7;
    }
}
