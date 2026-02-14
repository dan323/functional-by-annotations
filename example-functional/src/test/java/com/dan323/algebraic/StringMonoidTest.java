package com.dan323.algebraic;

import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.compiler.util.SemigroupUtil;
import com.dan323.functional.data.writer.StringConcatMonoid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringMonoidTest {

    @Test
    public void concat() {
        assertEquals("ab", StringConcatMonoid.op("a", "b"));
    }

    @Test
    public void neutral() {
        assertEquals("", StringConcatMonoid.unit());
    }

    @Test
    public void concatRefl() {
        assertEquals("cdd", SemigroupUtil.op(StringConcatMonoid.getInstance(), "cd", "d"));
    }

    @Test
    public void neutRefl() {
        assertEquals("", MonoidUtil.unit(StringConcatMonoid.getInstance()));
    }

}
