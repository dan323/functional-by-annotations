package com.dan323.functional.data.state;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.data.pair.Pair;

import java.util.function.Function;

@Monad
public class StateMonad<S> implements IMonad<State<?, S>> {

    private StateMonad() {
    }

    private static final StateMonad<?> STATE_FUNCTOR = new StateMonad<>();

    public static <R> StateMonad<R> getInstance() {
        return (StateMonad<R>) STATE_FUNCTOR;
    }

    public <A, B> State<B, S> map(State<A, S> state, Function<A, B> fun) {
        return s -> state
                .apply(s)
                .mapFirst(fun);
    }

    public <A> State<A, S> pure(A a) {
        return s -> new Pair<>(a, s);
    }

    public <A, B> State<B, S> fapply(State<Function<A, B>, S> functionState, State<A, S> state) {
        return s -> functionState
                .apply(s)
                .mapSecond(state)
                .biMap((f, p) -> p.mapFirst(f).getKey(), (f, p) -> p.getValue());
    }

    public <A> State<A, S> join(State<State<A, S>, S> stateSState) {
        return s -> stateSState.apply(s)
                .map(Function::apply);
    }

    public <A, B> State<B, S> flatMap(Function<A, State<B, S>> fun, State<A, S> state) {
        return s -> state
                .apply(s)
                .mapFirst(fun)
                .map(Function::apply);
    }

    @Override
    public Class<State<?, S>> getClassAtRuntime() {
        return (Class<State<?, S>>) (Class<?>) State.class;
    }
}
