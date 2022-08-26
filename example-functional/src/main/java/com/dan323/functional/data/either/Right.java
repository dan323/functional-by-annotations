package com.dan323.functional.data.either;

import java.util.Objects;
import java.util.function.Function;

public final class Right<A,B> implements Either<A,B> {

    private final B b;

    Right(B b){
        this.b = b;
    }

    public <C> C either(Function<A,C> aToC, Function<B,C> bToC){
        if (bToC == null){
            throw new IllegalArgumentException();
        } else {
            return bToC.apply(b);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        return Objects.equals(b, ((Right<A, B>) obj).b);
    }

    @Override
    public int hashCode() {
        return 11 * Objects.hashCode(b);
    }


    @Override
    public String toString() {
        return "R(" + b.toString() + ")";
    }
}
