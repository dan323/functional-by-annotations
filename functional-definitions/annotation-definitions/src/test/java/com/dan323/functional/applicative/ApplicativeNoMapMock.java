package com.dan323.functional.applicative;

import com.dan323.functional.Identity;
import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.Function;

@Applicative
public final class ApplicativeNoMapMock implements IApplicative<Identity<?>> {

    private ApplicativeNoMapMock(){

    }

    public static final ApplicativeNoMapMock APPLICATIVE = new ApplicativeNoMapMock();
    public static <A> Identity<A> pure(A a) {
        return new Identity<>(a);
    }

    public static <A, B> Identity<B> fapply(Identity<Function<A, B>> f, Identity<A> base) {
        return new Identity<>(f.get().apply(base.get()));
    }
}