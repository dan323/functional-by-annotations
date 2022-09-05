package com.dan323.functional.util.semigroup;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;

@Semigroup
public class EmptySemigroup implements ISemigroup<Integer> {

    private EmptySemigroup() {

    }

    public static final EmptySemigroup SEMIGROUP = new EmptySemigroup();

}
