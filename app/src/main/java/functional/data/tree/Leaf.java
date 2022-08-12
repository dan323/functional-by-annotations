package functional.data.tree;

import functional.data.optional.Maybe;

public final class Leaf<A> implements BinaryTree<A> {

    private final A a;

    Leaf(A a) {
        this.a = a;
    }

    public A value() {
        return a;
    }
}
