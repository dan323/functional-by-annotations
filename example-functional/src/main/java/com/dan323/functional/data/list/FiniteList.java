package com.dan323.functional.data.list;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Finite lists are represented by the empty or a finite amount con appends
 *
 * @param <A>
 */
public sealed interface FiniteList<A> extends List<A> permits FinCons, Nil {

    static <A> FiniteList<A> cons(A a, FiniteList<A> tail) {
        return new FinCons<>(a, tail);
    }

    static <A> FiniteList<A> nil() {
        return (FiniteList<A>) Nil.NIL;
    }

    /**
     * This default implementation only works finite lists, since otherwise the program does not finish
     *
     * @param mapping function to transform the elements
     * @param <B>     final type
     * @return transformed list
     * @see Repeat
     * @see Generating
     * @see Generating.GeneratingMapped
     */
    @Override
    default <B> FiniteList<B> map(Function<A, B> mapping) {
        return head().maybe(h -> FiniteList.cons(mapping.apply(h), tail().map(mapping)), nil());
    }

    FiniteList<A> tail();

    @SafeVarargs
    static <A> FiniteList<A> of(A... a) {
        if (a.length == 0) {
            return nil();
        } else {
            return FiniteList.of(0, a);
        }
    }

    @SafeVarargs
    private static <A> FiniteList<A> of(int n, A... a) {
        if (n >= a.length) {
            return nil();
        } else {
            return cons(a[n], of(n + 1, a));
        }
    }
}
