package com.dan323.functional.util.semigroup;

import com.dan323.functional.annotation.compiler.util.SemigroupUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SemigroupUtilTest {
    @Test
    public void semigroupOp() {
        var sol = SemigroupUtil.op(SemigroupInt.SEMIGROUP, 5, 6);
        assertEquals(11, sol);
    }

    @Test
    public void semigroupComposition() {
        var sol = SemigroupUtil.op(new SemigroupComposition<>(SemigroupInt.SEMIGROUP), 5, 6);
        assertEquals(11, sol);
    }

    @Test
    public void semigroupNoAnnotationError() {
        assertThrows(IllegalArgumentException.class, () -> SemigroupUtil.op(SemigroupNoAnnotation.SEMIGROUP, 5, 6));
    }

    @Test
    public void semigroupNoMethodError() {
        assertThrows(IllegalArgumentException.class, () -> SemigroupUtil.op(EmptySemigroup.SEMIGROUP, 5, 6));
    }
}
