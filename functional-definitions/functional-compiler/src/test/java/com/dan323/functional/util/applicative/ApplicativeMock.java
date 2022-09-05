package com.dan323.functional.util.applicative;

import com.dan323.functional.util.Identity;
import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.Function;

@Applicative
public final class ApplicativeMock implements IApplicative<Identity<?>> {

    private ApplicativeMock(){

    }

    @Override
    public Class<Identity<?>> getClassAtRuntime() {
        return (Class<Identity<?>>)(Class) Identity.class;
    }

    public static final ApplicativeMock APPLICATIVE = new ApplicativeMock();

    public <A> Identity<A> pure(A a) {
        return new Identity<>(a);
    }

    public <A, B> Identity<B> fapply(Identity<Function<A, B>> f, Identity<A> base) {
        return new Identity<>(f.get().apply(base.get()));
    }

    public static <A, B> Identity<B> map(Identity<A> base, Function<A, B> function) {
        return new Identity<>(function.apply(base.get()));
    }
}
