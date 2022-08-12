package functional.data.tree;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public sealed interface BinaryTree<A> extends IFunctor<BinaryTree<?>> permits BTree, Leaf {

    static <A> BinaryTree<A> leaf(A a) {
        return new Leaf<>(a);
    }

    static <A> BinaryTree<A> node(BinaryTree<A> left, BinaryTree<A> right) {
        return new BTree<>(left, right);
    }

    static <A, B> BinaryTree<B> map(BinaryTree<A> base, Function<A, B> mapping) {
        if (base instanceof Leaf<A> baseLeaf) {
            return new Leaf<>(mapping.apply(baseLeaf.value()));
        } else {
            BTree<A> baseTree = (BTree<A>) base;
            return new BTree<>(map(baseTree.left(), mapping), map(baseTree.right(), mapping));
        }
    }

}
