package com.dan323.functional.monoid;

import com.dan323.functional.annotation.algs.IMonoid;

public class MonoidNoAnnotation implements IMonoid<Integer> {

    private MonoidNoAnnotation(){

    }

    public static final MonoidNoAnnotation MONOID = new MonoidNoAnnotation();
    public static Integer op(Integer a, Integer b){
        return a+b;
    }

    public static Integer unit(){
        return 0;
    }
}
