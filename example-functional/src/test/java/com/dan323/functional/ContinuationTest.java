package com.dan323.functional;

import com.dan323.functional.annotation.util.ApplicativeUtil;
import com.dan323.functional.annotation.util.FunctorUtil;
import com.dan323.functional.annotation.util.MonadUtil;
import com.dan323.functional.data.continuation.Continuation;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContinuationTest {

    private Continuation<Function<Boolean, Boolean>, String> cont;

    @Test
    public void continuationTest() {
        Function<Integer, Continuation<Integer, String>> succ = q -> k -> k.apply(q + 1);
        assertEquals("7", succ.apply(6).apply(k -> Integer.toString(k)));
    }

    @Test
    public void continuationFunctor() {
        Function<Integer, Continuation<Integer, String>> succ = q -> k -> k.apply(q + 1);
        Continuation<Integer, String> mapped = Continuation.map(succ.apply(7), k -> k + 2);
        assertEquals("10", mapped.apply(k -> Integer.toString(k)));

        Continuation<Integer, String> cont = FunctorUtil.<Continuation, Continuation, Continuation<Integer, String>, Continuation<Integer, String>, Integer, Integer>map(Continuation.class, Continuation.class, succ.apply(9), k -> k - 1);
        assertEquals("9", cont.apply(k -> Integer.toString(k)));
    }

    @Test
    public void continuationApplicative() {
        Continuation<Function<Boolean, Boolean>, String> cont = k -> k.apply(b -> !b);
        Continuation<Boolean, String> mapped = k -> k.apply(false);
        var sol = ApplicativeUtil.<Continuation, Continuation, Continuation<Boolean, String>, Continuation<Boolean, String>, Continuation<Function<Boolean, Boolean>, String>>fapply(Continuation.class, Continuation.class, mapped, cont);
        assertEquals("true", sol.apply(b -> Boolean.toString(b)));

        sol = Continuation.fapply(cont, mapped);
        assertEquals("true", sol.apply(b -> Boolean.toString(b)));
    }

    @Test
    public void continuationPure() {
        Continuation<Integer, Boolean> sol = Continuation.pure(5);
        assertEquals(true, sol.apply(k -> k < 8));

        sol = ApplicativeUtil.pure(Continuation.class, Continuation.class, 8);
        assertEquals(false, sol.apply(k -> k > 9));
    }

    @Test
    public void continuationJoin() {
        Continuation<Continuation<Integer, Integer>, Integer> continuation = k -> k.apply(q -> q.apply(10));
        assertEquals(11, Continuation.join(continuation).apply(k -> k + 1));

        continuation = k -> k.apply(q -> q.apply(10));
        assertEquals(11, MonadUtil.<Continuation, Continuation, Continuation<Continuation<Integer, Integer>, Integer>, Continuation<Integer, Integer>>join(Continuation.class, Continuation.class, continuation).apply(k -> k + 1));
    }
}
