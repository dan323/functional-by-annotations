package com.dan323.functional.data.list.zipper;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.data.list.FiniteList;
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
@Functor
public class ListZipper<A> {

    private final FiniteList<A> left;
    private final FiniteList<A> right;

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
        return left.head().maybe(x -> Maybe.of(new ListZipper<>(left.tail(), x, right)), Maybe.of());
    }

    public Maybe<ListZipper<A>> moveRight() {
        return right.head().maybe(x -> Maybe.of(new ListZipper<>(FiniteList.cons(x, left), right.tail())), Maybe.of());
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

}
