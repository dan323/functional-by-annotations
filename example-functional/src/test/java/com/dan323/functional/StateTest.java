package com.dan323.functional;

import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.data.Unit;
import com.dan323.functional.data.Void;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.state.State;
import com.dan323.functional.data.state.StateMonad;
import com.dan323.functional.data.state.StateWithError;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static com.dan323.functional.data.state.State.fromFun;
import static com.dan323.functional.data.state.StateWithError.fromStateWithVoidError;
import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    @Test
    public void stateFunctor() {
        StateMonad<Integer, Void> stateMonad = StateMonad.getInstance();

        State<Boolean, Integer> trueState = StateWithError.fromStateWithVoidError(stateMonad.pure(true));
        State<Boolean, Integer> falseState = StateWithError.fromStateWithVoidError(stateMonad.pure(false));
        State<Boolean, Integer> greater5Sate = fromFun(s -> new Pair<>(s >= 5, s));

        assertTrue(Either.leftVoid(trueState.evaluate(5)));
        assertFalse(Either.leftVoid(falseState.evaluate(5)));
        assertTrue(Either.leftVoid(greater5Sate.evaluate(5)));
        assertFalse(Either.leftVoid(greater5Sate.evaluate(2)));
        var state = stateMonad.map(trueState, x -> x ? 7 : 9);
        assertEquals(7, Either.leftVoid(state.evaluate(4)));
        state = stateMonad.map(falseState, x -> x ? 7 : 9);
        assertEquals(9, Either.leftVoid(state.evaluate(4)));
    }

    @Test
    public void stateGet() {
        State<Integer, Integer> st = fromStateWithVoidError(StateWithError.get());
        assertEquals(4, Either.leftVoid(st.execute(4)));
        assertEquals(5, Either.leftVoid(st.evaluate(5)));
    }

    @Test
    public void statePut() {
        State<Unit, Integer> st = fromStateWithVoidError(StateWithError.put(8));
        assertEquals(Unit.fromMVoid(Maybe.of()), Either.leftVoid(st.evaluate(7)));
        assertEquals(8, Either.leftVoid(st.execute(7)));
    }

    @Test
    public void stateFapply() {
        State<Integer, Integer> base = fromFun(s -> new Pair<>(4, s - 1));
        State<Function<Integer, Integer>, Integer> ff = fromFun(s -> new Pair<>(x -> x + 4, s - 1));

        var k = ApplicativeUtil.fapply(StateMonad.<Integer, Void>getInstance(), base, ff);

        assertEquals(5, Either.leftVoid(k.execute(7)));
        assertEquals(8, Either.leftVoid(k.evaluate(7)));
    }

    @Test
    public void stateJoin() {
        StateMonad<Boolean, Void> stateMonad = StateMonad.getInstance();

        State<StateWithError<Integer, Boolean, Void>, Boolean> state = fromFun(s -> new Pair<>(stateWith(s), !s));

        assertEquals(9, Either.leftVoid(stateMonad.join(state).evaluate(true)));
        assertEquals(8, Either.leftVoid(stateMonad.join(state).evaluate(false)));
        assertEquals(false, Either.leftVoid(stateMonad.join(state).execute(false)));
        assertEquals(true, Either.leftVoid(stateMonad.join(state).execute(true)));
    }

    private State<Integer, Boolean> stateWith(Boolean s) {
        return fromFun(r -> new Pair<>(r ? 8 : 9, !r));
    }
}
