package com.dan323.functional.data.tree;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public final class BTreeFunctor implements IFunctor<BinaryTree<?>> {

    private BTreeFunctor() {
    }

    public static final BTreeFunctor FUNCTOR = new BTreeFunctor();

    public static <A, B> BinaryTree<B> map(BinaryTree<A> base, Function<A, B> mapping) {
        return base.map(mapping);
    }

    public static <A> BinaryTree<A> pure(A a) {
        return BinaryTree.node(BinaryTree.leaf(), a, BinaryTree.leaf());
    }

    @Override
    public Class<BinaryTree<?>> getClassAtRuntime() {
        return (Class<BinaryTree<?>>) (Class<?>) BinaryTree.class;
    }
}
