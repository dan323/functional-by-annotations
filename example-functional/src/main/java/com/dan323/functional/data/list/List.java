package com.dan323.functional.data.list;

import com.dan323.functional.data.optional.Maybe;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public sealed interface List<A> permits FiniteList, InfiniteList {

    Maybe<A> head();

    List<A> tail();

    List<A> cons(A head);

    <B> List<B> map(Function<A,B> mapping);

    static <A> InfiniteList<A> generate(A first, UnaryOperator<A> generator){
        return new Generating<>(first, generator);
    }

    default FiniteList<A> limit(int k){
        return head().maybe(h -> limitWithHead(h, k), List.nil());
    }

    private FiniteList<A> limitWithHead(A h, int k){
        if (k == 0){
            return List.nil();
        } else {
            return FiniteList.cons(h, tail().limit(k-1));
        }
    }

    static <A> InfiniteList<A> interleave(InfiniteList<A> fa, InfiniteList<A> fb) {
        return new Merged<>(fa, fb);
    }

    static <A> List<A> cycle(List<A> lst){
        if (lst instanceof FiniteList<A> fl) {
            return cycle(fl);
        } else {
            return lst;
        }
    }

    private static <A> List<A> cycle(FiniteList<A> lst){
        if (lst.length() == 0) {
            return nil();
        } else if (lst.length() == 1) {
            return lst.head().maybe(List::repeat,nil());
        } else {
            return new Cycle<>(lst);
        }
    }

    static <A> FiniteList<A> nil() {
        return (FiniteList<A>) Nil.NIL;
    }

    static <A> InfiniteList<A> repeat(A a){
        return new Repeat<>(a);
    }
}