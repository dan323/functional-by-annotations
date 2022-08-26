package com.dan323.functional;

import com.dan323.functional.data.function.EndoMonoid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EndomorphismTest {

    @Test
    public void endTest() {
        EndoMonoid<Integer> endoMonoid = EndoMonoid.getInstance();
        assertEquals(11, endoMonoid.op(x -> x + 1, x -> x * 2).apply(5));
        assertEquals(5, endoMonoid.unit().apply(5));
    }
}
