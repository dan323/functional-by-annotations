package com.dan323.mock;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
@Applicative
@Semigroup
public class SomeApplicative implements IFunctor<List<?>>, IApplicative<List<?>>, ISemigroup<Integer> {

    public static <A> List<A> pure(A a) {
        return List.of(a);
    }

    public static Integer op(Integer a, Integer b) {
        return (a + b) % 7;
    }

    public static <A, B> List<B> map(List<A> base, Function<A, B> fun) {
        return base.stream().map(fun).toList();
    }

    public static <A, B> List<B> fapply(List<Function<A, B>> funList, List<A> base) {
        return funList.stream().flatMap(f -> base.stream().map(f)).toList();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class)List.class;
    }
}
