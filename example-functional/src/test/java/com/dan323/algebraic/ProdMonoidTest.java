package com.dan323.algebraic;

import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.compiler.util.SemigroupUtil;
import com.dan323.functional.data.integer.ProdMonoid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProdMonoidTest {

    @Test
    public void prod() {
        assertEquals(20, ProdMonoid.op(4, 5));
    }

    @Test
    public void neutral() {
        assertEquals(1, ProdMonoid.unit());
    }

    @Test
    public void prodRefl() {
        assertEquals(20, SemigroupUtil.op(ProdMonoid.getInstance(), 4, 5));
    }

    @Test
    public void neutRefl() {
        assertEquals(1, MonoidUtil.unit(ProdMonoid.getInstance()));
    }

}
