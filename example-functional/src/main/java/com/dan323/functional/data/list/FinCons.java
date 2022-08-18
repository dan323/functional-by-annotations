package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

import java.util.Objects;

final class FinCons<A> implements FiniteList<A> {

    private final Cons<A> cons;

    FinCons(A head, FiniteList<A> tail) {
        cons = new Cons<>(head, tail);
    }

    @Override
    public Maybe<A> head() {
        return cons.head();
    }

    @Override
    public FiniteList<A> tail() {
        return (FiniteList<A>) cons.tail();
    }


    @Override
    public String toString() {
        return cons.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        FinCons<?> k = ((FinCons<?>) obj);
        return Objects.equals(k.cons, cons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cons, "finite");
    }
}
