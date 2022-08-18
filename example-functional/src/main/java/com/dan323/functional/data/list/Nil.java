package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

/**
 * Empty list
 * @param <A>
 */
final class Nil<A> implements FiniteList<A> {

    private Nil() {
    }

    static Nil<?> NIL = new Nil<>();

    @Override
    public Maybe<A> head() {
        return Maybe.of();
    }

    @Override
    public FiniteList<A> tail() {
        return new Nil<>();
    }

    @Override
    public String toString() {
        return "[]";
    }
/*
    @Override
    public <R> List<R> map(Function<A, R> f) {
        return (List<R>) NIL;
    }

    @Override
    public <R> List<R> flatMap(Function<A, Monad<R, List<?>>> f) {
        return (List<R>) NIL;
    }

    @Override
    public <Q> List<Q> fapply(Applicative<Function<A, Q>, List<?>> ff) {
        return List.nil();
    }*/

    @Override
    public boolean equals(Object obj) {
        return obj == NIL;
    }

    @Override
    public int hashCode() {
        return 7;
    }
}
