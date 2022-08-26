package com.dan323.functional.data.util.applicative;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.util.ApplicativeUtil;
import com.dan323.functional.annotation.util.MonoidUtil;

@Monoid
public class ApplicativeMonoid<FA extends F, F, A> extends ApplicativeSemigroup<FA, F, A> implements IMonoid<FA> {

    public ApplicativeMonoid(IMonoid<A> monoid, IApplicative<F> applicative) {
        super(monoid, applicative);
    }

    public FA unit() {
        return ApplicativeUtil.pure(applicative, fClass, MonoidUtil.unit(monoid));
    }

}
