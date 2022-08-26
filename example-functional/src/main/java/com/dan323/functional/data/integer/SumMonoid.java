package com.dan323.functional.data.integer;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public final class SumMonoid implements IMonoid<Integer> {

    private SumMonoid() {
    }

    public static SumMonoid getInstance() {
        return SUM_MONOID;
    }

    public static Integer op(Integer num1, Integer num2) {
        return num1 + num2;
    }

    public static Integer unit() {
        return 0;
    }

    private static final SumMonoid SUM_MONOID = new SumMonoid();
}
