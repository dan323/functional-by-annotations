package com.dan323.functional.data.state;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.pair.Pair;

import java.util.function.Function;

@Monad
public class StateMonad<S, E> implements IMonad<StateWithError<?, S, E>> {

    private StateMonad() {
    }

    private static final StateMonad<?, ?> STATE_FUNCTOR = new StateMonad<>();

    public static <R, E> StateMonad<R, E> getInstance() {
        return (StateMonad<R, E>) STATE_FUNCTOR;
    }

    public <A, B> StateWithError<B, S, E> map(StateWithError<A, S, E> state, Function<A, B> fun) {
        return s -> state
                .apply(s)
                .either(Either::left, k -> Either.right(k.mapFirst(fun)));
    }

    public <A> StateWithError<A, S, E> pure(A a) {
        return s -> Either.right(new Pair<>(a, s));
    }

    public <A, B> StateWithError<B, S, E> fapply(StateWithError<Function<A, B>, S, E> functionState, StateWithError<A, S, E> state) {
        return s -> functionState
                .apply(s)
                .either(
                        Either::<E, Pair<B, S>>left,
                        p1 -> state.apply(p1.getValue()).either(Either::left, p2 -> Either.right(new Pair<>(p1.getKey().apply(p2.getKey()), p2.getValue())))
                );
    }

    public <A> StateWithError<A, S, E> join(StateWithError<StateWithError<A, S, E>, S, E> stateState) {
        return s -> stateState
                .apply(s)
                .either(Either::left, p -> p.getKey().apply(p.getValue()));
    }

    public <A, B> StateWithError<B, S, E> flatMap(Function<A, StateWithError<B, S, E>> fun, StateWithError<A, S, E> state) {
        return s -> state
                .apply(s)
                .either(Either::left, p -> p.mapFirst(fun).map(Function::apply));
    }

    @Override
    public Class<StateWithError<?, S, E>> getClassAtRuntime() {
        return (Class<StateWithError<?, S, E>>) (Class<?>) StateWithError.class;
    }
}
