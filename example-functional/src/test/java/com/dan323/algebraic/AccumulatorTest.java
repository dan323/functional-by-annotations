package com.dan323.algebraic;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.writer.Accumulator;
import com.dan323.functional.data.writer.Writer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccumulatorTest {

    private final Accumulator<String> accumulator = new Accumulator<>();

    @Test
    public void pureUsesEmptyLog() {
        Writer<Integer, FiniteList<String>> writer = accumulator.pure(10);
        assertEquals(10, writer.execute());
        assertEquals(FiniteList.of(), writer.log());
    }

    @Test
    public void mapPreservesLog() {
        Writer<Integer, FiniteList<String>> writer =
                accumulator.map(Writer.tell(FiniteList.of("a")), _ -> 3);
        assertEquals(3, writer.execute());
        assertEquals(FiniteList.of("a"), writer.log());
    }

    @Test
    public void flatMapConcatsLogsInOrder() {
        Writer<Integer, FiniteList<String>> base =
                accumulator.map(Writer.tell(FiniteList.of("start")), _ -> 5);
        Writer<Integer, FiniteList<String>> result = accumulator.flatMap(
                value -> accumulator.map(Writer.tell(FiniteList.of("next", "end")), _ -> value + 1),
                base
        );
        assertEquals(6, result.execute());
        assertEquals(FiniteList.of("start", "next", "end"), result.log());
    }

    @Test
    public void fapplyCombinesFunctionLogFirst() {
        Writer<Integer, FiniteList<String>> base =
                accumulator.map(Writer.tell(FiniteList.of("b")), _ -> 2);
        Writer<java.util.function.Function<Integer, Integer>, FiniteList<String>> fn =
                accumulator.map(Writer.tell(FiniteList.of("f", "g")), _ -> x -> x + 1);
        Writer<Integer, FiniteList<String>> result = accumulator.fapply(base, fn);
        assertEquals(3, result.execute());
        assertEquals(FiniteList.of("f", "g", "b"), result.log());
    }

    @Test
    public void joinConcatsOuterThenInnerLogs() {
        Writer<Writer<Integer, FiniteList<String>>, FiniteList<String>> outer =
                accumulator.map(Writer.tell(FiniteList.of("o")),
                        _ -> accumulator.map(Writer.tell(FiniteList.of("i1", "i2")), in -> 5));
        Writer<Integer, FiniteList<String>> result = accumulator.join(outer);
        assertEquals(5, result.execute());
        assertEquals(FiniteList.of("o", "i1", "i2"), result.log());
    }

    @Test
    public void liftA2CombinesLogsLeftToRight() {
        Writer<Integer, FiniteList<String>> left =
                accumulator.map(Writer.tell(FiniteList.of("a")), _ -> 2);
        Writer<Integer, FiniteList<String>> right =
                accumulator.map(Writer.tell(FiniteList.of("b", "c")), _ -> 3);
        Writer<Integer, FiniteList<String>> result = accumulator.liftA2(Integer::sum, left, right);
        assertEquals(5, result.execute());
        assertEquals(FiniteList.of("a", "b", "c"), result.log());
    }
}

