package com.dan323.functional.data.state;

import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.optional.Maybe;

import java.util.function.Function;

public interface State<A, S> extends Function<S, Pair<A, S>> {

    default S execute(S s) {
        return apply(s).getValue();
    }

    default A evaluate(S s) {
        return apply(s).getKey();
    }

    static <S> State<S, S> get() {
        return s -> new Pair<>(s, s);
    }

    static <S> State<Maybe<Void>, S> put(S x) {
        return s -> new Pair<>(Maybe.of(), x);
    }

}
