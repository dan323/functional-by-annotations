package com.dan323.functional.data.tree;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

public sealed interface BinaryTree<A> permits BinaryNode, Leaf {

    static <A> BinaryTree<A> leaf() {
        return (BinaryTree<A>) Leaf.LEAF;
    }

    static <A> BinaryTree<A> node(BinaryTree<A> left, A point, BinaryTree<A> right) {
        return new BinaryNode<>(left, point, right);
    }

}
