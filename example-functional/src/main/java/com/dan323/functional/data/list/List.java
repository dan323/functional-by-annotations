package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.data.optional.Maybe;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public sealed interface List<A> permits Cons, FiniteList, Generating, Generating.GeneratingMapped, Repeat, Zipped {

    Maybe<A> head();

    List<A> tail();

    <B> List<B> map(Function<A,B> mapping);

    default FiniteList<A> limit(int k){
        return head().maybe(h -> limitWithHead(h, k), FiniteList.nil());
    }

    private FiniteList<A> limitWithHead(A h, int k){
        if (k == 0){
            return FiniteList.nil();
        } else {
            return FiniteList.cons(h, tail().limit(k-1));
        }
    }

    static <A> List<A> generate(A first, UnaryOperator<A> generator){
        return new Generating<>(first, generator);
    }

    static <A> List<A> cons(A first, List<A> tail) {
        if (first == null || tail == null) {
            throw new IllegalArgumentException("No input can be null");
        }
        if (tail instanceof FiniteList<A> finiteTail){
            return new FinCons<>(first, finiteTail);
        } else {
            return new Cons<>(first, tail);
        }
    }

    static <A> List<A> nil() {
        return (FiniteList<A>) Nil.NIL;
    }

    static <A> List<A> repeat(A a){
        return new Repeat<>(a);
    }
}