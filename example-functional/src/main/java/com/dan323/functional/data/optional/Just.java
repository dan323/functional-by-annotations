package com.dan323.functional.data.optional;

import java.util.Objects;
import java.util.function.Function;

public final class Just<A> implements Maybe<A> {

    private final A element;

    Just(A element) {
        this.element = element;
    }

    @Override
    public <C> C maybe(Function<A, C> f, C constant) {
        return f.apply(element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Just<?> just = (Just<?>) o;
        return Objects.equals(element, just.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element);
    }

    @Override
    public String toString() {
        return "Just["+element.toString()+"]";
    }
}
