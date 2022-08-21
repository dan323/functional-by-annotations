package com.dan323.functional.data.sum;

import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

public class Prod<M, N, A> {

    private final M a;
    private final N b;

    Prod(M a, N b) {
        this.a = a;
        this.b = b;
    }

    public M getFirst() {
        return a;
    }

    public N getSecond() {
        return b;
    }
}
