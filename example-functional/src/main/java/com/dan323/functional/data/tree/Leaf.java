package com.dan323.functional.data.tree;

import java.util.function.Function;

public final class Leaf<A> implements BinaryTree<A> {

    private Leaf() {
    }

    static final Leaf<?> LEAF = new Leaf<>();

    @Override
    public <B> BinaryTree<B> map(Function<A,B> f) {
        @SuppressWarnings("unchecked")
        BinaryTree<B> leaf = (BinaryTree<B>) LEAF;
        return leaf;
    }

    @Override
    public int hashCode() {
        return 85;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Leaf;
    }

    @Override
    public String toString() {
        return "l[]";
    }
}
