package com.dan323.functional.annotation.compiler.applicative;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.List;
import java.util.function.Function;

@Applicative
class NoPublicTypeApplicative implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a){
        return List.of(a);
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }

    public static <A,B> List<B> fapply(List<Function<A, B>> ffunction, List<A> base){
        return List.of();
    }
}
