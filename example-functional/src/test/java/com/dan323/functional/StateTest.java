package com.dan323.functional;

import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.state.State;
import com.dan323.functional.data.state.StateMonad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StateTest {

    @Test
    public void stateFunctor() {
        StateMonad<Integer> stateMonad = StateMonad.getInstance();

        State<Boolean, Integer> trueState = stateMonad.pure(true);
        State<Boolean, Integer> falseState = stateMonad.pure(false);
        State<Boolean, Integer> greater5Sate = s -> new Pair<>(s >= 5, s);

        assertTrue(trueState.evaluate(5));
        assertFalse(falseState.evaluate(5));
        assertTrue(greater5Sate.evaluate(5));
        assertFalse(greater5Sate.evaluate(2));
        var state = stateMonad.map(trueState, x -> x ? 7 : 9);
        assertEquals(7, state.evaluate(4));
        state = stateMonad.map(falseState, x -> x ? 7 : 9);
        assertEquals(9, state.evaluate(4));
    }

    @Test
    public void stateJoin() {
        StateMonad<Boolean> stateMonad = StateMonad.getInstance();

        State<State<Integer, Boolean>, Boolean> state = s -> new Pair<>(stateWith(s), !s);

        assertEquals(9, stateMonad.join(state).evaluate(true));
        assertEquals(8, stateMonad.join(state).evaluate(false));
        assertEquals(false, stateMonad.join(state).execute(false));
        assertEquals(true, stateMonad.join(state).execute(true));
    }

    private State<Integer, Boolean> stateWith(Boolean s) {
        return r -> new Pair<>(r ? 8 : 9, !r);
    }
}
