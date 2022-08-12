package functional.data.list.zipper;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;
import functional.data.list.FiniteList;
import functional.data.list.FiniteListFunctor;
import functional.data.list.List;
import functional.data.optional.Maybe;

import java.util.function.Function;

import static functional.data.list.ListUtils.concat;
import static functional.data.list.ListUtils.reverse;

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
        return concat(reverse(left), FiniteList.cons(pointer, right));
    }

    public static <A, B> ListZipper<B> map(ListZipper<A> base, Function<A, B> mapping) {
        return new ListZipper<>(FiniteListFunctor.map(base.left, mapping), mapping.apply(base.pointer), FiniteListFunctor.map(base.right, mapping));
    }

}
