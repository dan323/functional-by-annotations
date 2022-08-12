package functional.data.list;

import functional.data.optional.Maybe;

public final class ListUtils {

    public static <A> FiniteList<A> reverse(FiniteList<A> lst) {
        return lst.head().maybe(h -> concat(reverse(lst.tail()), FiniteList.cons(h, FiniteList.nil())), FiniteList.nil());
    }

    public static <A> List<A> concat(List<A> a, List<A> b) {
        if (b instanceof Nil<A>) {
            return a;
        } else if (a instanceof Repeat<A> || a instanceof Generating<A> || a instanceof Generating.GeneratingMapped<?, ?>) {
            return a;
        } else {
            return a.head().maybe(h -> List.cons(h, concat(a.tail(), b)), b);
        }
    }

    public static <A> FiniteList<A> concat(FiniteList<A> a, FiniteList<A> b){
        return a.head().maybe(h -> FiniteList.cons(h, concat(a.tail(), b)), b);
    }

    static <C> List<C> join(List<List<C>> lst) {
        Maybe<List<C>> first = lst.head();
        return first.maybe(list -> {
            if (list instanceof Repeat<C> || list instanceof Generating<C> || list instanceof Generating.GeneratingMapped<?, ?>) {
                return list;
            } else {
                return ListUtils.concat(list, join(lst.tail()));
            }
        }, List.nil());
    }
}
