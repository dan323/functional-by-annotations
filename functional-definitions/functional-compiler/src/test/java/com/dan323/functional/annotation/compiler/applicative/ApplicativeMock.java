package com.dan323.functional.annotation.compiler.applicative;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.compiler.functor.FunctorMock;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.List;
import java.util.function.Function;

@Applicative
public class ApplicativeMock extends FunctorMock<Integer> implements IApplicative<List<?>> {

    public ApplicativeMock(List<Integer> lst) {
        super(lst);
    }

    public static <A> List<A> pure(A a) {
        return List.of(a);
    }

    public static <A, B> List<B> fapply(List<Function<A, B>> ffunction, List<A> base) {
        return ffunction.stream().flatMap(f -> base.stream().map(f)).toList();
    }

}
