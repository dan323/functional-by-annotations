package com.dan323.functional.annotation.compiler.applicative;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.List;

@Applicative
public class ApplicativeOnlyPure implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a){
        return List.of();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
