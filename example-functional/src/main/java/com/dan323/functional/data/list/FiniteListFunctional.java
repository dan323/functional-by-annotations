package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.Function;

import static com.dan323.functional.data.list.ListUtils.concat;

@Monad
@Monoid
public final class FiniteListFunctional<A> implements IMonad<FiniteList<?>>, IMonoid<FiniteList<A>> {

    private FiniteListFunctional(){
    }

    private static final FiniteListFunctional<?> FINITE_LIST_FUNCTIONAL = new FiniteListFunctional<>();

    public static <A> FiniteListFunctional<A> getInstance(){
        return (FiniteListFunctional<A>) FINITE_LIST_FUNCTIONAL;
    }

    public static <A,B> FiniteList<B> map(FiniteList<A> finiteList, Function<A,B> mapping){
        return finiteList.head().maybe(h -> FiniteList.cons(mapping.apply(h), map(finiteList.tail(), mapping)), FiniteList.nil());
    }

    public static <A> FiniteList<A> op(FiniteList<A> op1, FiniteList<A> op2){
        return ListUtils.concat(op1,op2);
    }

    public static <A> FiniteList<A> unit(){
        return FiniteList.nil();
    }

    public static <A> FiniteList<A> pure(A a){
        return FiniteList.of(a);
    }

    public static <A,B> FiniteList<B> flatMap(Function<A,FiniteList<B>> f, FiniteList<A> base){
        return base.head().maybe(h -> concat(f.apply(h),flatMap(f, base.tail())), FiniteList.nil());
    }
}
