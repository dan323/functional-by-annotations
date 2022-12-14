package com.dan323.functional.data.integer;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public final class ProdMonoid implements IMonoid<Integer> {

    private ProdMonoid(){}

    public static ProdMonoid getInstance(){
        return PROD_MONOID;
    }

    public static Integer op(Integer num1, Integer num2) {
        return num1 * num2;
    }

    public static Integer unit() {
        return 1;
    }

    private static final ProdMonoid PROD_MONOID = new ProdMonoid();
}
