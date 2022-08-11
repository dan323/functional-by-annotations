package functional.data.list;

import functional.data.optional.Optional;

public final class ListUtils {

    public static <A> List<A> reverse(List<A> lst) {
        return lst.head().maybe(h -> concat(reverse(lst.tail()), List.cons(h, List.nil())), List.nil());
    }

    public static <A> List<A> concat(List<A> a, List<A> b) {
        if (b instanceof Nil<A>) {
            return a;
        } else {
            return a.head().maybe(h -> List.cons(h, concat(a.tail(), b)), b);
        }
    }

    static <C> List<C> join(List<List<C>> lst) {
        Optional<List<C>> first = lst.head();
        return first.maybe(list ->
                ListUtils.concat(list, join(lst.tail())), List.nil());
    }
}
