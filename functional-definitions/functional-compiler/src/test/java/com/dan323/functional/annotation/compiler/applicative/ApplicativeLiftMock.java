package com.dan323.functional.annotation.compiler.applicative;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.List;
import java.util.function.BiFunction;

@Applicative
public class ApplicativeLiftMock implements IApplicative<List<?>> {

    public static <A, B, C> List<C> liftA2(BiFunction<A, B, C> function, List<A> base1, List<B> base2) {
        return base1.stream().flatMap(a -> base2.stream().map(b -> function.apply(a, b))).toList();
    }

    public static <A> List<A> pure(A a){
        return List.of(a);
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}