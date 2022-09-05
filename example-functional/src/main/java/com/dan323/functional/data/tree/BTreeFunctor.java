package com.dan323.functional.data.tree;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public class BTreeFunctor implements IFunctor<BinaryTree<?>> {

    private BTreeFunctor(){

    }

    public static final BTreeFunctor FUNCTOR = new BTreeFunctor();

    public static <A, B> BinaryTree<B> map(BinaryTree<A> base, Function<A, B> mapping) {
        if (base instanceof Leaf<A> baseLeaf) {
            return new Leaf<>(mapping.apply(baseLeaf.value()));
        } else {
            BinaryNode<A> baseTree = (BinaryNode<A>) base;
            return new BinaryNode<>(map(baseTree.left(), mapping), map(baseTree.right(), mapping));
        }
    }


    @Override
    public Class<BinaryTree<?>> getClassAtRuntime() {
        return (Class<BinaryTree<?>>) (Class)BinaryTree.class;
    }
}
