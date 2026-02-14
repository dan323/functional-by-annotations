package com.dan323.algebraic;

import com.dan323.functional.data.writer.LoggerMonad;
import com.dan323.functional.data.writer.Writer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoggerMonadTest {

    private final LoggerMonad loggerMonad = new LoggerMonad();

    @Test
    public void pureUsesEmptyLog() {
        Writer<Integer, String> writer = loggerMonad.pure(7);
        assertEquals(7, writer.execute());
        assertEquals("", writer.log());
    }

    @Test
    public void mapPreservesLog() {
        Writer<Integer, String> writer = loggerMonad.map(Writer.tell("log"), ignored -> 5);
        assertEquals(5, writer.execute());
        assertEquals("log", writer.log());
    }

    @Test
    public void flatMapConcatsLogsInOrder() {
        Writer<Integer, String> base = loggerMonad.map(Writer.tell("a"), ignored -> 1);
        Writer<Integer, String> result = loggerMonad.flatMap(
                value -> loggerMonad.map(Writer.tell("b"), ignored -> value + 1),
                base
        );
        assertEquals(2, result.execute());
        assertEquals("ab", result.log());
    }

    @Test
    public void fapplyCombinesFunctionLogFirst() {
        Writer<Integer, String> base = loggerMonad.map(Writer.tell("b"), _ -> 2);
        Writer<java.util.function.Function<Integer, Integer>, String> fn =
                loggerMonad.map(Writer.tell("f"), _ -> x -> x + 1);
        Writer<Integer, String> result = loggerMonad.fapply(base, fn);
        assertEquals(3, result.execute());
        assertEquals("fb", result.log());
    }

    @Test
    public void joinConcatsOuterThenInnerLogs() {
        Writer<Writer<Integer, String>, String> outer =
                loggerMonad.map(Writer.tell("o"), _ -> loggerMonad.map(Writer.tell("i"), in -> 5));
        Writer<Integer, String> result = loggerMonad.join(outer);
        assertEquals(5, result.execute());
        assertEquals("oi", result.log());
    }

    @Test
    public void liftA2CombinesLogsLeftToRight() {
        Writer<Integer, String> left = loggerMonad.map(Writer.tell("a"), _ -> 2);
        Writer<Integer, String> right = loggerMonad.map(Writer.tell("b"), _ -> 3);
        Writer<Integer, String> result = loggerMonad.liftA2(Integer::sum, left, right);
        assertEquals(5, result.execute());
        assertEquals("ab", result.log());
    }
}
