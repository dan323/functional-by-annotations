package com.dan323.functional.semigroup;

import com.dan323.functional.annotation.algs.ISemigroup;

public class SemigroupNoAnnotation implements ISemigroup<Integer> {
    public static Integer op(Integer a, Integer b){
        return a+b;
    }
}
