package com.dan323.functional.data.list;

import java.util.function.BiFunction;
import java.util.function.Function;

public final class Merged<A> extends InfiniteList<A>{

    private final InfiniteList<A> listLeft;
    private final InfiniteList<A> listRight;

    Merged(InfiniteList<A> listLeft, InfiniteList<A> listRight){
        if (listLeft == null || listRight == null){
            throw new IllegalArgumentException("Inputs must not be null");
        }
        this.listLeft = listLeft;
        this.listRight = listRight;
    }

    @Override
    public A getHead() {
        return listLeft.getHead();
    }

    @Override
    public InfiniteList<A> tail() {
        return new Merged<>(listRight, listLeft.tail());
    }

    @Override
    public <B> InfiniteList<B> map(Function<A, B> mapping) {
        return new Merged<>(listLeft.map(mapping), listRight.map(mapping));
    }

    public <B,C> InfiniteList<C> zipBy(BiFunction<A, B, C> mapper, Merged<B> other) {
        return new Merged<>((InfiniteList<C>)ZipApplicative.liftA2(mapper,listLeft, other.listLeft), (InfiniteList<C>) ZipApplicative.liftA2(mapper,listRight, other.listRight));
    }
}
