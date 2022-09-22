package com.dan323.functional.data.tree;

import java.util.Objects;

public final class BinaryNode<A> implements BinaryTree<A> {
    private final BinaryTree<A> left;
    private final A data;
    private final BinaryTree<A> right;

    BinaryNode(BinaryTree<A> left, A data, BinaryTree<A> right) {
        this.left = left;
        this.data = data;
        this.right = right;
    }

    public BinaryTree<A> left() {
        return left;
    }

    public BinaryTree<A> right() {
        return right;
    }

    public A data() {
        return data;
    }

    @Override
    public int hashCode() {
        return 23 * left.hashCode() + 51 * right.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof BinaryNode<?> binaryNode) {
            return Objects.equals(binaryNode.left, left) && Objects.equals(binaryNode.right, right);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "[(" + left + ";" + data + ";" + right + ")]";
    }
}
