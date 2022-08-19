package com.dan323.functional.monoid;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public class MonoidSum implements IMonoid<Integer> {

    public static Integer op(Integer a, Integer b){
        return a+b;
    }

    public static Integer unit(){
        return 0;
    }
}
