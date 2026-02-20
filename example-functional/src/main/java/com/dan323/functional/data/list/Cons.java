package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * List of one element in front of another infinite list
 *
 * @param <A>
 */
final class Cons<A> extends InfiniteList<A> {

    private final A head;
    private final InfiniteList<A> tail;

    Cons(A head, InfiniteList<A> tail) {
        if (head == null || tail == null) {
            throw new IllegalArgumentException();
        }
        this.head = head;
        this.tail = tail;
    }

    @Override
    public A getHead() {
        return head;
    }

    @Override
    public InfiniteList<A> tail() {
        return tail;
    }

    @Override
    public <B> InfiniteList<B> map(Function<A, B> mapping) {
        return new Cons<>(mapping.apply(head), tail().map(mapping));
    }

    public <B,C> InfiniteList<C> zipBy(BiFunction<A, B, C> mapper, InfiniteList<B> list) {
        var newHead = mapper.apply(head,list.getHead());
        return new Cons<>(newHead, (InfiniteList<C>) ZipApplicative.liftA2(mapper, tail, list.tail()));
    }

    @Override
    public String toString() {
        return "[" + head + "," + tail + "]";
    }

}