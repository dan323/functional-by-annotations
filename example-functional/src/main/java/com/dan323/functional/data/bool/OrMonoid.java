package com.dan323.functional.data.bool;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public final class OrMonoid implements IMonoid<Boolean> {

    private OrMonoid(){}

    public static Boolean op(Boolean b1, Boolean b2){
        return b1 || b2;
    }

    public static Boolean unit(){
        return Boolean.FALSE;
    }

    public static final OrMonoid OR_MONOID = new OrMonoid();
}
