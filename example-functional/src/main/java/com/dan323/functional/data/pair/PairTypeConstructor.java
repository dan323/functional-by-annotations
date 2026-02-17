package com.dan323.functional.data.pair;

import java.util.Objects;

/**
 * A pair constructor
 * @param <M> Some type F_1<A>
 * @param <N> Some type F_2<A>
 * @param <A>
 */
public final class PairTypeConstructor<M, N, A> {

    private final M a;
    private final N b;

    public PairTypeConstructor(M a, N b) {
        this.a = a;
        this.b = b;
    }

    public M getFirst() {
        return a;
    }

    public N getSecond() {
        return b;
    }

    @Override
    public int hashCode() {
        return 3* Objects.hashCode(a) + 7* Objects.hashCode(b);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof PairTypeConstructor<?,?,?> ptc){
            return Objects.equals(ptc.a, a) && Objects.equals(ptc.b, b);
        } else {
            return false;
        }
    }
}
