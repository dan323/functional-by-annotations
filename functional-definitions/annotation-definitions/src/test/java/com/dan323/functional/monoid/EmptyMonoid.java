package com.dan323.functional.monoid;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public class EmptyMonoid implements IMonoid<Integer> {

    private EmptyMonoid(){

    }

    public static final EmptyMonoid MONOID = new EmptyMonoid();
}
