package com.dan323.functional.data.list;

public final class ListUtils {

    public static <A> FiniteList<A> reverse(FiniteList<A> lst) {
        return lst.head().maybe(h -> concat(reverse(lst.tail()), FiniteList.cons(h, FiniteList.nil())), FiniteList.nil());
    }

    public static <A> FiniteList<A> concat(FiniteList<A> a, FiniteList<A> b){
        return a.head().maybe(h -> concat(a.tail(), b).cons(h), b);
    }

    public static <A> InfiniteList<A> concat(FiniteList<A> a, InfiniteList<A> b){
        return a.head().maybe(h -> concat(a.tail(), b).cons(h), b);
    }
}
