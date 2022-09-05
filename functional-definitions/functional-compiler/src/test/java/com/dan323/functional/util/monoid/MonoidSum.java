package com.dan323.functional.util.monoid;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public class MonoidSum implements IMonoid<Integer> {

    private MonoidSum(){

    }

    public static final MonoidSum MONOID = new MonoidSum();

    public Integer op(Integer a, Integer b){
        return a+b;
    }

    public Integer unit(){
        return 0;
    }
}
