package com.dan323.functional.data.util.applicative;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.SemigroupUtil;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.lang.reflect.Type;

@Semigroup
public class ApplicativeSemigroup<FA extends F, F, A> implements ISemigroup<FA> {

    protected final ISemigroup<A> monoid;
    protected final IApplicative<? extends F> applicative;

    public ApplicativeSemigroup(ISemigroup<A> monoid, IApplicative<? extends F> applicative) {
        this.applicative = applicative;
        this.monoid = monoid;
    }

    public FA op(FA elem1, FA elem2) {
        return (FA) ApplicativeUtil.liftA2(applicative, (A x, A y) -> SemigroupUtil.op(monoid, x, y), elem1, elem2);
    }
}
