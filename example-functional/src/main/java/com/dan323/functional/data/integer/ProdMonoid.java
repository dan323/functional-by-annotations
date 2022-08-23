package com.dan323.functional.data.integer;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public final class ProdMonoid implements IMonoid<Integer> {

    public Integer op(Integer num1, Integer num2) {
        return num1 * num2;
    }

    public Integer unit() {
        return 1;
    }
}
