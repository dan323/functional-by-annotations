package com.dan323.functional;

import com.dan323.functional.data.function.FunctionFrom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionFromTest {

    @Test
    public void functionFromFunctor() {
        var fun = FunctionFrom.<Integer, String, String>map(Integer::parseInt, String::valueOf);

        assertEquals("9", fun.apply("9"));
    }

    @Test
    public void functionFromPure() {
        var fun = FunctionFrom.<Integer, Integer>pure(9);

        assertEquals(9, fun.apply(8));
    }

    @Test
    public void functionFromFapply() {
        var fun = FunctionFrom.<Integer, Integer, Integer>fapply(x -> (y -> y - x), u -> u * 2);

        assertEquals(8, fun.apply(8));
    }

    @Test
    public void functionFromLiftA2() {
        var fun = FunctionFrom.<Integer, Integer, Integer, Integer>liftA2((a, b) -> a * b, u -> u * 3, v -> v * 2);

        assertEquals(24, fun.apply(2));
    }

    @Test
    public void functionFromJoin() {
        var fun = FunctionFrom.<Integer, Integer>join(x -> (y -> x + y * 7));

        assertEquals(56, fun.apply(7));
    }

    @Test
    public void functionFromFlatMap(){
        var fun = FunctionFrom.<Integer,Integer,Integer>flatMap(x -> (y-> Math.max(x,y)), y -> y*2-1);

        assertEquals(5, fun.apply(3));
    }
}
