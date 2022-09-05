package com.dan323.functional.data.util.applicative;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.funcs.IApplicative;

@Monoid
public class ApplicativeMonoid<FA extends F, F, A> extends ApplicativeSemigroup<FA, F, A> implements IMonoid<FA> {

    public ApplicativeMonoid(IMonoid<A> monoid, IApplicative<F> applicative) {
        super(monoid, applicative);
    }

    public FA unit() {
        return (FA) ApplicativeUtil.pure(applicative, MonoidUtil.unit((IMonoid<A>)monoid));
    }

}
