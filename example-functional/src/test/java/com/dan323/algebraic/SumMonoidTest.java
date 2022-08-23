package com.dan323.algebraic;

import com.dan323.functional.annotation.util.MonoidUtil;
import com.dan323.functional.annotation.util.SemigroupUtil;
import com.dan323.functional.data.integer.SumMonoid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SumMonoidTest {

    @Test
    public void sum() {
        assertEquals(9, SumMonoid.SUM_MONOID.op(4, 5));
    }

    @Test
    public void neutral() {
        assertEquals(0, SumMonoid.SUM_MONOID.unit());
    }

    @Test
    public void sumRefl() {
        assertEquals(9, SemigroupUtil.op(SumMonoid.SUM_MONOID, 4, 5));
    }

    @Test
    public void neutRefl() {
        assertEquals(0, MonoidUtil.<SumMonoid, Integer>unit(SumMonoid.SUM_MONOID));
    }

}
