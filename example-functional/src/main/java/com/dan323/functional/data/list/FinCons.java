package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

import java.util.Objects;

final class FinCons<A> implements FiniteList<A> {

    private final A head;
    private final FiniteList<A> tail;

    FinCons(A head, FiniteList<A> tail) {
        this.head = head;
        this.tail = tail;
    }

    public int length(){
        return 1 + tail.length();
    }

    @Override
    public Maybe<A> head() {
        return Maybe.of(head);
    }

    @Override
    public FiniteList<A> tail() {
        return tail;
    }


    @Override
    public String toString() {
        return "[" + head + "," + tail + "]";
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        FinCons<?> k = ((FinCons<?>) obj);
        return Objects.equals(k.head, head) && Objects.equals(k.tail, tail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(head, tail, "finite");
    }
}
