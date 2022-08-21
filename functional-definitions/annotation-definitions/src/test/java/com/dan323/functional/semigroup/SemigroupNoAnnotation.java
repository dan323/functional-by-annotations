package com.dan323.functional.semigroup;

import com.dan323.functional.annotation.algs.ISemigroup;

public class SemigroupNoAnnotation implements ISemigroup<Integer> {

    private SemigroupNoAnnotation() {

    }

    public static final SemigroupNoAnnotation SEMIGROUP = new SemigroupNoAnnotation();
    public static Integer op(Integer a, Integer b){
        return a+b;
    }
}
