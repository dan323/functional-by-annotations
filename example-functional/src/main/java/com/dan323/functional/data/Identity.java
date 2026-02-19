package com.dan323.functional.data;

public final class Identity<A> {

    private final A a;

    Identity(A a) {
        this.a = a;
    }

    public static <A> Identity<A> of(A a) {
        return new Identity<>(a);
    }

    public static <B> B runIdentity(Identity<B> b) {
        return b.a;
    }
}
