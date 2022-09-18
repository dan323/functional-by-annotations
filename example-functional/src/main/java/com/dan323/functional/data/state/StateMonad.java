package com.dan323.functional.data.state;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.data.Pair;

import java.util.function.Function;

@Monad
public class StateMonad<S> implements IMonad<State<?, S>> {

    public StateMonad() {
    }

    private static final StateMonad<?> STATE_FUNCTOR = new StateMonad<>();

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
                .map((f, p) -> p.mapFirst(f).getKey(), (f, p) -> p.getValue());
    }

    public <A> State<A,S> join(State<State<A,S>,S> stateSState) {
        return s -> stateSState.apply(s)
                .map(Function::apply, (st, s1) -> s1)
                .getKey();
    }

    public <A,B> State<B,S> flatMap(Function<A,State<B,S>> fun, State<A,S> state) {
        return s -> state
                .apply(s)
                .mapFirst(fun)
                .map(Function::apply,(st, s1) -> s1)
                .getKey();
    }

    @Override
    public Class<State<?, S>> getClassAtRuntime() {
        return (Class<State<?, S>>) (Class<?>) State.class;
    }
}
