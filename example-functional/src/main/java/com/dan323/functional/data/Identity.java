package com.dan323.functional.data;

public final class Identity<A> {

    private final A a;

    Identity(A a) {
        this.a = a;
    }

    public static <B> B runIdentity(Identity<B> b) {
        return b.a;
    }
}
