package com.dan323.functional.data.tree;

import java.util.function.Function;

public sealed interface BinaryTree<A> permits BinaryNode, Leaf {

    static <A> BinaryTree<A> leaf() {
        return (BinaryTree<A>) Leaf.LEAF;
    }

    <B> BinaryTree<B> map(Function<A,B> f);

    static <A> BinaryTree<A> node(BinaryTree<A> left, A point, BinaryTree<A> right) {
        return new BinaryNode<>(left, point, right);
    }

}
