package functional.data.tree;

import functional.data.optional.Maybe;

public final class BTree<A> implements BinaryTree<A>{
    private final BinaryTree<A> left;
    private final BinaryTree<A> right;

    BTree(BinaryTree<A> left, BinaryTree<A> right){
        this.left = left;
        this.right = right;
    }

    public BinaryTree<A> left() {
        return left;
    }

    public BinaryTree<A> right() {
        return right;
    }
}
