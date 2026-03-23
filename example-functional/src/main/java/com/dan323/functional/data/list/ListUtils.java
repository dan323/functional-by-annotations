package com.dan323.functional.data.list;

public final class ListUtils {

    public static <A> FiniteList<A> reverse(FiniteList<A> lst) {
        return lst.head().maybe(h -> concat(reverse(lst.tail()), FiniteList.cons(h, List.nil())), List.nil());
    }

    public static <A> FiniteList<A> concat(FiniteList<A> a, FiniteList<A> b) {
        return a.head().maybe(h -> FiniteList.cons(h, concat(a.tail(), b)), b);
    }

    private static <A> List<A> concat(FiniteList<A> a, List<A> b){
        return a.head().maybe(h -> concat(a.tail(), b).cons(h), b);
    }

    public static <A> List<A> concat(List<A> a, List<A> b){
        if (a instanceof FiniteList<A> fa) {
            return concat(fa, b);
        } else {
            return a;
        }
    }
}
