package com.dan323.functional.semigroup;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.util.SemigroupUtil;

@Semigroup
public class SemigroupComposition<A> implements ISemigroup<A> {

    private final ISemigroup<A> semigroup;

    SemigroupComposition(ISemigroup<A> semigroup){
        this.semigroup = semigroup;
    }

    public A op(A a, A b){
        return SemigroupUtil.op(semigroup, a, b);
    }
}
