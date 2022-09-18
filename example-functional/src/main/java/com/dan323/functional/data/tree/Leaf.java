package com.dan323.functional.data.tree;

public final class Leaf<A> implements BinaryTree<A> {

    private Leaf() {
    }

    static final Leaf<?> LEAF = new Leaf<>();

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
