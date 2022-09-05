package com.dan323.functional.util.applicative;

import com.dan323.functional.util.Identity;
import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.List;
import java.util.function.BiFunction;

@Applicative
public final class ApplicativeLiftA2Mock implements IApplicative<Identity<?>> {

    private ApplicativeLiftA2Mock(){

    }

    @Override
    public Class<Identity<?>> getClassAtRuntime() {
        return (Class<Identity<?>>)(Class) Identity.class;
    }

    public static final ApplicativeLiftA2Mock APPLICATIVE = new ApplicativeLiftA2Mock();
    public <A> Identity<A> pure(A a) {
        return new Identity<>(a);
    }

    public <A, B, C> Identity<C> liftA2(BiFunction<A, B, C> f, Identity<A> fa, Identity<B> fb) {
        return pure(f.apply(fa.get(), fb.get()));
    }
}