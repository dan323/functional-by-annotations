package com.dan323.functional.semigroup;

import com.dan323.functional.annotation.util.SemigroupUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SemigroupUtilTest {
    @Test
    public void semigroupOp() {
        var sol = SemigroupUtil.op(SemigroupInt.SEMIGROUP, Integer.class, 5, 6);
        assertEquals(11, sol);
    }

    @Test
    public void semigroupNoAnnotationError(){
        assertThrows(IllegalArgumentException.class, () -> SemigroupUtil.op(SemigroupNoAnnotation.SEMIGROUP, Integer.class, 5, 6));
    }

    @Test
    public void semigroupNoMethodError(){
        assertThrows(IllegalArgumentException.class, () -> SemigroupUtil.op(EmptySemigroup.SEMIGROUP, Integer.class, 5, 6));
    }
}
