package com.dan323.functional.data.list.zipper;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.list.ListUtils;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;

import java.util.function.Function;

/**
 * Zipper representing the list rev(left)++[pointer]++right
 * <p>
 * It represents only finite lists
 *
 * @param <A>
 * @author Daniel de la Concepcion
 */
public final class ListZipper<A> {

    private final FiniteList<A> left;
    private final FiniteList<A> right;

    public int index(){
        return left.length();
    }

    ListZipper(FiniteList<A> left, A pointer, FiniteList<A> right) {
        this(left, FiniteList.cons(pointer, right));
    }

    ListZipper(FiniteList<A> left, FiniteList<A> right) {
        if (left == null || right == null) {
            throw new IllegalArgumentException("No input can be null");
        }
        this.left = left;
        this.right = right;
    }

    ListZipper(FiniteList<A> finiteList) {
        if (finiteList == null) {
            throw new IllegalArgumentException("No input can be null");
        }
        this.left = FiniteList.nil();
        this.right = finiteList;
    }

    public Maybe<A> get() {
        return right.head();
    }

    FiniteList<A> getLeft() {
        return left;
    }

    FiniteList<A> getRight() {
        return right.tail();
    }

    public static <A> ListZipper<A> zipFrom(FiniteList<A> original) {
        return new ListZipper<>(original);
    }

    public Maybe<ListZipper<A>> moveLeft() {
        return MaybeMonad.map(left.head(), x -> new ListZipper<A>(left.tail(), x, right));
    }

    public Maybe<ListZipper<A>> moveRight() {
        return MaybeMonad.map(right.head(), x -> new ListZipper<>(FiniteList.cons(x, left), right.tail()));
    }

    public ListZipper<A> modify(Function<A, A> map) {
        return MaybeMonad.map(MaybeMonad.map(right.head(), map), h -> FiniteList.cons(h, right.tail())).maybe(r -> new ListZipper<>(left, r), this);
    }

    public ListZipper<A> set(A b) {
        return modify(x -> b);
    }

    public List<A> toList() {
        return ListUtils.concat(ListUtils.reverse(left), right);
    }

    @Override
    public String toString() {
        return toList().toString();
    }
}
