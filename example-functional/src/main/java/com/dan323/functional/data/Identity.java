package com.dan323.functional.data;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

public class Identity<A> {

    private final A a;

    Identity(A a) {
        this.a = a;
    }

    public static <B> B runIdentity(Identity<B> b) {
        return b.a;
    }
}
