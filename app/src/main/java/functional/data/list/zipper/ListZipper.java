package functional.data.list.zipper;

import functional.annotation.iface.IFunctor;
import functional.data.list.List;
import functional.data.optional.Maybe;

import java.util.function.Function;

import static functional.data.list.ListUtils.concat;
import static functional.data.list.ListUtils.reverse;

/**
 * Zipper representing the list rev(left)++[pointer]++right
 *
 * @param <A>
 * @author Daniel de la Concepcion
 */
public class ListZipper<A> implements IFunctor<ListZipper<?>> {

    private final List<A> left;
    private final A pointer;
    private final List<A> right;

    ListZipper(List<A> left, A pointer, List<A> right) {
        if (left == null || pointer == null || right == null) {
            throw new IllegalArgumentException("No input can be null");
        }
        this.left = left;
        this.right = right;
        this.pointer = pointer;
    }

    public Maybe<ListZipper<A>> moveLeft() {
        var newLeft = left.tail();
        var newRight = List.cons(pointer, right);
        return left.head().maybe(x -> Maybe.of(new ListZipper<>(newLeft, x, newRight)), Maybe.of());
    }

    public Maybe<ListZipper<A>> moveRight() {
        var newLeft = List.cons(pointer, left);
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
        return concat(reverse(left), List.cons(pointer, right));
    }

    public static <A, B> ListZipper<B> map(ListZipper<A> base, Function<A, B> mapping) {
        return new ListZipper<>(List.map(base.left, mapping), mapping.apply(base.pointer), List.map(base.right, mapping));
    }

}
