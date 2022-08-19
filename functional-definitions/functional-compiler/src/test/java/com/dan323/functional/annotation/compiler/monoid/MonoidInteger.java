package com.dan323.functional.annotation.compiler.monoid;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;

@Monoid
public class MonoidInteger implements IMonoid<Integer> {

    public static Integer op(Integer a, Integer b){
        return a+b;
    }

    public static Integer unit(){
        return 0;
    }
}
