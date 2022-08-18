package com.dan323.functional.semigroup;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;

@Semigroup
public class SemigroupInt implements ISemigroup<Integer> {

    public static Integer op(Integer a, Integer b){
        return a+b;
    }
}
