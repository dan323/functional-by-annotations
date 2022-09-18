package com.dan323.functional.data.optional;

import java.util.Objects;
import java.util.function.Function;

public final class Nothing<A> implements Maybe<A> {

    private Nothing(){
    }

    static final Nothing<?> NOTHING = new Nothing<>();

    @Override
    public <C> C maybe(Function<A, C> f, C constant) {
        return constant;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == NOTHING;
    }

    @Override
    public int hashCode(){
        return 90;
    }

    @Override
    public String toString() {
        return "NOTHING";
    }
}
