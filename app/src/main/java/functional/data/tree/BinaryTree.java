package functional.data.tree;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public sealed interface BinaryTree<A> extends IFunctor<BinaryTree<?>> permits BinaryNode, Leaf {

    static <A> BinaryTree<A> leaf(A a) {
        return new Leaf<>(a);
    }

    static <A> BinaryTree<A> node(BinaryTree<A> left, BinaryTree<A> right) {
        return new BinaryNode<>(left, right);
    }

    static <A, B> BinaryTree<B> map(BinaryTree<A> base, Function<A, B> mapping) {
        if (base instanceof Leaf<A> baseLeaf) {
            return new Leaf<>(mapping.apply(baseLeaf.value()));
        } else {
            BinaryNode<A> baseTree = (BinaryNode<A>) base;
            return new BinaryNode<>(map(baseTree.left(), mapping), map(baseTree.right(), mapping));
        }
    }

}
