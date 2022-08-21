package com.dan323.functional.applicative;

import com.dan323.functional.Identity;
import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.BiFunction;

@Applicative
public final class ApplicativeLiftA2Mock implements IApplicative<Identity<?>> {

    private ApplicativeLiftA2Mock(){

    }

    public static final ApplicativeLiftA2Mock APPLICATIVE = new ApplicativeLiftA2Mock();
    public <A> Identity<A> pure(A a) {
        return new Identity<>(a);
    }

    public <A, B, C> Identity<C> liftA2(BiFunction<A, B, C> f, Identity<A> fa, Identity<B> fb) {
        return pure(f.apply(fa.get(), fb.get()));
    }
}