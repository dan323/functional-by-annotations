package com.dan323.functional.data.tree;

import java.util.function.Function;

public final class Leaf<A> implements BinaryTree<A> {

    private Leaf() {
    }

    static final Leaf<?> LEAF = new Leaf<>();

    public <B> BinaryTree<B> map(Function<A,B> f) {
        return new Leaf<>();
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
