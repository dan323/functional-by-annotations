package com.dan323.functional.monoid;

import com.dan323.functional.annotation.util.MonoidUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MonoidUtilTest {
    @Test
    public void monoidUnit() {
        var sol = MonoidUtil.unit(MonoidSum.class, Integer.class);
        assertEquals(0, sol);
    }

    @Test
    public void monoidNoAnnotationError() {
        assertThrows(IllegalArgumentException.class, () -> MonoidUtil.unit(MonoidNoAnnotation.class, Integer.class));
    }

    @Test
    public void monoidNoUnitError() {
        assertThrows(IllegalArgumentException.class, () -> MonoidUtil.unit(EmptyMonoid.class, Integer.class));
    }
}
