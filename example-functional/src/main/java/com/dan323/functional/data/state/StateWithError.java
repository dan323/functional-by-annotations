package com.dan323.functional.data.state;

import com.dan323.functional.data.Unit;
import com.dan323.functional.data.Void;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.pair.Pair;

import java.util.function.Function;

@FunctionalInterface
public interface StateWithError<A, S, E> extends Function<S, Either<E, Pair<A, S>>> {

    static <A, S, E> StateWithError<A, S, E> fromState(State<A, S> state) {
        return s -> state.apply(s).either(Void::absurd, Either::right);
    }

    static <A, S> State<A, S> fromStateWithVoidError(StateWithError<A, S, Void> state) {
        return State.fromFun(s -> state.apply(s).either(Void::absurd, Function.identity()));
    }

    static <A,S,E> StateWithError<A,S,E> error(E error) {
        return s -> Either.left(error);
    }

    default Either<E,S> execute(S s) {
        return apply(s).either(Either::left, p -> Either.right(p.getValue()));
    }

    default Either<E,A> evaluate(S s) {
        return apply(s).either(Either::left, p -> Either.right(p.getKey()));
    }

    static <S,E> StateWithError<S, S, E> get() {
        return s -> Either.right(new Pair<>(s, s));
    }

    static <S,E> StateWithError<Unit, S, E> put(S x) {
        return s -> Either.right(new Pair<>(Unit.getElement(), x));
    }

}
