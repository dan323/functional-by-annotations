package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;

import java.util.function.BiFunction;
import java.util.function.Function;

@Applicative
public final class ZipApplicative implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a) {
        return List.repeat(a);
    }

    public static <A, B, C> List<C> liftA2(BiFunction<A, B, C> fun, List<A> lstA, List<B> lstB) {
        if (lstA instanceof FiniteList<A> finiteList) {
            return finiteList.head().maybe(a -> lstB.head().maybe(b -> FiniteList.cons(fun.apply(a, b), (FiniteList<C>) liftA2(fun, lstA.tail(), lstB.tail())), FiniteList.nil()), FiniteList.nil());
        } else if (lstB instanceof FiniteList<B> finiteList) {
            return finiteList.head().maybe(a -> lstA.head().maybe(b -> FiniteList.cons(fun.apply(b, a), (FiniteList<C>) liftA2(fun, lstA.tail(), lstB.tail())), FiniteList.nil()), FiniteList.nil());
        } else if (lstA instanceof Repeat<A> repeat) {
            Maybe<Function<B, C>> aux = MaybeMonad.map(repeat.head(), a -> ((B b) -> fun.apply(a, b)));
            return aux.maybe(lstB::map, List.nil());
        } else if (lstB instanceof Repeat<B> repeat) {
            Maybe<Function<A, C>> aux = MaybeMonad.map(repeat.head(), a -> ((A b) -> fun.apply(b, a)));
            return aux.maybe(lstA::map, List.nil());
        } else {
            return new Zipped<>(lstA, fun, lstB);
        }
    }

    public static <A, B> List<B> map(List<A> base, Function<A, B> map) {
        return base.map(map);
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class) List.class;
    }
}
