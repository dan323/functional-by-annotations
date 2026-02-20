package com.dan323.functional.data.list;

import java.util.function.BiFunction;
import java.util.function.Function;

final class Zipped<A, B, C> extends InfiniteList<C> {

    private final InfiniteList<A> first;
    private final InfiniteList<B> second;
    private final BiFunction<A, B, C> zipper;

    Zipped(InfiniteList<A> first, BiFunction<A, B, C> zipper, InfiniteList<B> second) {
        this.first = first;
        this.zipper = zipper;
        this.second = second;
    }

    @Override
    public InfiniteList<C> tail() {
        return new Zipped<>(first.tail(), zipper, second.tail());
    }

    @Override
    public <D> InfiniteList<D> map(Function<C, D> mapping) {
        return new Zipped<>(first, zipper.andThen(mapping), second);
    }

    @Override
    public C getHead() {
        return zipper.apply(first.getHead(), second.getHead());
    }

}
