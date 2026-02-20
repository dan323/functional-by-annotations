package com.dan323.functional.data.list;

import java.util.function.Function;
import java.util.function.UnaryOperator;

final class Generating<A> extends InfiniteList<A> {

    private final A head;
    private final UnaryOperator<A> generator;

    Generating(A first, UnaryOperator<A> generator){
        if (first == null || generator == null){
            throw new IllegalArgumentException("Inputs must not be null");
        }
        this.head = first;
        this.generator = generator;
    }

    @Override
    public A getHead() {
        return head;
    }

    @Override
    public InfiniteList<A> tail() {
        return new Generating<>(generator.apply(head), generator);
    }

    @Override
    public <B> InfiniteList<B> map(Function<A, B> mapping) {
        return new GeneratingMapped<>(this, mapping);
    }

    protected static final class GeneratingMapped<A,B> extends InfiniteList<B> {

        private final InfiniteList<A> originalList;
        private final Function<A,B> mapping;

        GeneratingMapped(InfiniteList<A> originalList, Function<A,B> mapping){
            this.mapping = mapping;
            this.originalList = originalList;
        }

        @Override
        public B getHead() {
            return mapping.apply(originalList.getHead());
        }

        @Override
        public InfiniteList<B> tail() {
            return new GeneratingMapped<>(originalList.tail(), mapping);
        }

        @Override
        public <B1> InfiniteList<B1> map(Function<B, B1> mapping) {
            return new GeneratingMapped<>(originalList, mapping.compose(this.mapping));
        }
    }
}
