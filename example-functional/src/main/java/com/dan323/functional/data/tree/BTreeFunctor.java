package com.dan323.functional.data.tree;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public class BTreeFunctor implements IFunctor<BinaryTree<?>> {

    private BTreeFunctor() {
    }

    public static final BTreeFunctor FUNCTOR = new BTreeFunctor();

    public static <A, B> BinaryTree<B> map(BinaryTree<A> base, Function<A, B> mapping) {
        if (base instanceof Leaf<A>) {
            return BinaryTree.leaf();
        } else if (base instanceof BinaryNode<A> node) {
            return BinaryTree.node(map(node.left(), mapping), mapping.apply(node.data()), map(node.right(), mapping));
        } else {
            throw new IllegalStateException("There are more instances that implemented");
        }
    }

    public static <A> BinaryTree<A> pure(A a) {
        return BinaryTree.node(BinaryTree.leaf(), a, BinaryTree.leaf());
    }

    @Override
    public Class<BinaryTree<?>> getClassAtRuntime() {
        return (Class<BinaryTree<?>>) (Class) BinaryTree.class;
    }
}
