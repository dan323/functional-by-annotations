package functional.data.tree;

import java.util.Objects;

public final class Leaf<A> implements BinaryTree<A> {

    private final A a;

    Leaf(A a) {
        if (a == null) throw new IllegalArgumentException("The data must not be null.");
        this.a = a;
    }

    public A value() {
        return a;
    }

    @Override
    public int hashCode() {
        return 85 * a.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Leaf<?> leaf) {
            return Objects.equals(leaf.a, a);
        } else {
            return false;
        }
    }
}
