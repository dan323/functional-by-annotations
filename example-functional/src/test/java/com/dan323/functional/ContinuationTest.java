package com.dan323.functional;

import com.dan323.functional.data.continuation.Continuation;
import com.dan323.functional.data.continuation.ContinuationMonad;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContinuationTest {

    @Test
    public void continuationTest() {
        Function<Integer, Continuation<Integer, String>> succ = q -> k -> k.apply(q + 1);
        assertEquals("7", succ.apply(6).apply(k -> Integer.toString(k)));
    }

    @Test
    public void continuationFunctor() {
        Function<Integer, Continuation<Integer, String>> succ = q -> k -> k.apply(q + 1);
        Continuation<Integer, String> mapped = ContinuationMonad.<String>getInstance().map(succ.apply(7), k -> k + 2);
        assertEquals("10", mapped.apply(k -> Integer.toString(k)));
    }

    @Test
    public void continuationApplicative() {
        Continuation<Function<Boolean, Boolean>, String> cont = k -> k.apply(b -> !b);
        Continuation<Boolean, String> mapped = k -> k.apply(false);

        var sol = ContinuationMonad.<String>getInstance().fapply(cont, mapped);
        assertEquals("true", sol.apply(b -> Boolean.toString(b)));
    }

    @Test
    public void continuationPure() {
        Continuation<Integer, Boolean> sol = ContinuationMonad.<Boolean>getInstance().pure(5);
        assertEquals(true, sol.apply(k -> k < 8));
    }

    @Test
    public void continuationJoin() {
        Continuation<Continuation<Integer, Integer>, Integer> continuation = k -> k.apply(q -> q.apply(10));
        assertEquals(11, ContinuationMonad.<Integer>getInstance().join(continuation).apply(k -> k + 1));
    }
}
