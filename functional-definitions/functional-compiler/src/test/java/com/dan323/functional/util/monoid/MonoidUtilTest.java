package com.dan323.functional.util.monoid;

import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonoidUtilTest {
    @Test
    public void monoidUnit() {
        var sol = MonoidUtil.unit(MonoidSum.MONOID);
        assertEquals(0, sol);
    }

    @Test
    public void monoidNoAnnotationError() {
        assertThrows(IllegalArgumentException.class, () -> MonoidUtil.unit(MonoidNoAnnotation.MONOID));
    }

    @Test
    public void monoidNoUnitError() {
        assertThrows(IllegalArgumentException.class, () -> MonoidUtil.unit(EmptyMonoid.MONOID));
    }
}
