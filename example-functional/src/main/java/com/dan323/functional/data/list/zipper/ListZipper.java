package com.dan323.functional.data.list.zipper;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.list.ListUtils;
import com.dan323.functional.data.optional.Maybe;

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
public class ListZipper<A> implements IFunctor<ListZipper<?>> {

    private final FiniteList<A> left;
    private final A pointer;
    private final FiniteList<A> right;

    ListZipper(FiniteList<A> left, A pointer, FiniteList<A> right) {
        if (left == null || pointer == null || right == null) {
            throw new IllegalArgumentException("No input can be null");
        }
        this.left = left;
        this.right = right;
        this.pointer = pointer;
    }

    public static <A> Maybe<ListZipper<A>> zipFrom(FiniteList<A> original) {
        if (original.equals(FiniteList.nil())) {
            return Maybe.of();
        } else {
            return original.head().maybe(h -> Maybe.of(new ListZipper<>(FiniteList.nil(), h, original.tail())), Maybe.of());
        }
    }

    public Maybe<ListZipper<A>> moveLeft() {
        var newLeft = left.tail();
        var newRight = FiniteList.cons(pointer, right);
        return left.head().maybe(x -> Maybe.of(new ListZipper<>(newLeft, x, newRight)), Maybe.of());
    }

    public Maybe<ListZipper<A>> moveRight() {
        var newLeft = FiniteList.cons(pointer, left);
        var newRight = right.tail();
        return right.head().maybe(x -> Maybe.of(new ListZipper<>(newLeft, x, newRight)), Maybe.of());
    }

    public ListZipper<A> modify(Function<A, A> map) {
        return new ListZipper<>(left, map.apply(pointer), right);
    }

    public ListZipper<A> set(A b) {
        return modify(x -> b);
    }

    public List<A> toList() {
        return ListUtils.concat(ListUtils.reverse(left), FiniteList.cons(pointer, right));
    }

    public static <A, B> ListZipper<B> map(ListZipper<A> base, Function<A, B> mapping) {
        return new ListZipper<>(FiniteListFunctional.map(base.left, mapping), mapping.apply(base.pointer), FiniteListFunctional.map(base.right, mapping));
    }

}
