package com.dan323.functional.util;

import com.dan323.functional.data.util.foldable.FoldableFuns;
import com.dan323.mock.SomeFoldable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FoldableFunsTest {

    @Test
    public void emptyTest() {
        var empty = FoldableFuns.isEmpty(new SomeFoldable(), List.class, List.of());
        assertTrue(empty);
        empty = FoldableFuns.isEmpty(new SomeFoldable(), List.class, List.of(1, 2, 3));
        assertFalse(empty);
    }

    @Test
    public void containsTest() {
        var hasIt = FoldableFuns.contains(new SomeFoldable(), List.class, 4, List.of());
        assertFalse(hasIt);
        hasIt = FoldableFuns.contains(new SomeFoldable(), List.class, 2, List.of(1, 2, 3));
        assertTrue(hasIt);
        hasIt = FoldableFuns.contains(new SomeFoldable(), List.class, 5, List.of(1, 2, 3));
        assertFalse(hasIt);
    }

    @Test
    public void lengthTest() {
        var len = FoldableFuns.length(new SomeFoldable(), List.class, List.of());
        assertEquals(0, len);
        len = FoldableFuns.length(new SomeFoldable(), List.class, List.of(1, 2, 3));
        assertEquals(3, len);
    }
}
