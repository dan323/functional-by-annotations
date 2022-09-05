package com.dan323.functional.util.semigroup;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;

@Semigroup
public class SemigroupInt implements ISemigroup<Integer> {

    private SemigroupInt() {

    }

    public static final SemigroupInt SEMIGROUP = new SemigroupInt();
    public static Integer op(Integer a, Integer b){
        return a+b;
    }
}
