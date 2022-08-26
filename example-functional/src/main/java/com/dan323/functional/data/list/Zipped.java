package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;

import java.util.function.BiFunction;
import java.util.function.Function;

final class Zipped<A, B, C> implements List<C> {

    private final List<A> first;
    private final List<B> second;
    private final BiFunction<A, B, C> zipper;


    Zipped(List<A> first, BiFunction<A, B, C> zipper, List<B> second) {
        this.first = first;
        this.zipper = zipper;
        this.second = second;
    }

    @Override
    public Maybe<C> head() {
        var aux = MaybeMonad.map(first.head(), x -> (Function<B, C>) ((B b) -> zipper.apply(x, b)));
        return MaybeMonad.fapply(aux, second.head());
    }

    @Override
    public List<C> tail() {
        return new Zipped<>(first.tail(), zipper, second.tail());
    }

    @Override
    public <D> List<D> map(Function<C, D> mapping) {
        return new Zipped<>(first, zipper.andThen(mapping), second);
    }

    @Override
    public <D, E> List<E> zip(BiFunction<C, D, E> zipper, List<D> list) {
        return new Zipped<>(this, zipper, list);
    }
}
