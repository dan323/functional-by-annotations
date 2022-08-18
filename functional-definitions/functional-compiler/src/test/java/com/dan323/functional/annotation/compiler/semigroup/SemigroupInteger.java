package com.dan323.functional.annotation.compiler.semigroup;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;

@Semigroup
public class SemigroupInteger implements ISemigroup<Integer> {

    public static Integer op(Integer a, Integer b){
        return a+b;
    }
}
