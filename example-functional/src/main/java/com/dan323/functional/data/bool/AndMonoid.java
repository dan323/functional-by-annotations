package com.dan323.functional.data.bool;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public final class AndMonoid implements IMonoid<Boolean> {

    private AndMonoid(){}

    public Boolean op(Boolean f1, Boolean f2) {
        return f1 && f2;
    }

    public Boolean unit() {
        return Boolean.TRUE;
    }

    public static final AndMonoid AND_MONOID = new AndMonoid();

}
