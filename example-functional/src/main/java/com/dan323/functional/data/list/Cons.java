package com.dan323.functional.data.list;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * List of one element in front of another infinite list
 * @param <A>
 */
final class Cons<A> implements InfiniteList<A> {

    private final A head;
    private final InfiniteList<A> tail;

    Cons(A head, InfiniteList<A> tail) {
        if (head == null || tail == null){
            throw new IllegalArgumentException();
        }
        this.head = head;
        this.tail = tail;
    }

    @Override
    public Maybe<A> head() {
        return Maybe.of(head);
    }

    @Override
    public InfiniteList<A> tail() {
        return tail;
    }

    @Override
    public <B> InfiniteList<B> map(Function<A, B> mapping) {
        return new Cons<>(mapping.apply(head), tail().map(mapping));
    }

    @Override
    public String toString(){
        return "[" + head + "," + tail + "]";
    }

    @Override
    public boolean equals(Object obj){
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Cons<?> k = ((Cons<?>)obj);
        return Objects.equals(k.head, head) && Objects.equals(k.tail, tail);
    }

    @Override
    public int hashCode(){
        return Objects.hash(head, tail);
    }
/*
    @Override
    public <Q> List<Q> fapply(Applicative<Function<A, Q>, List<?>> ff) {
        var fg = (List<Function<A,Q>>) ff;
        return fg.head().maybe(h -> ListUtils.concat(this.map(h), fapply(fg.tail())), List.nil());
    }*/
}