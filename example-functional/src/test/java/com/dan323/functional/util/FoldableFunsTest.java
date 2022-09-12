package com.dan323.functional.util;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.util.foldable.FoldableFuns;
import com.dan323.mock.SomeFoldable;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FoldableFunsTest {

    @Test
    public void emptyTest() {
        var empty = FoldableFuns.isEmpty(new SomeFoldable(), List.of());
        assertTrue(empty);
        empty = FoldableFuns.isEmpty(new SomeFoldable(), List.of(1, 2, 3));
        assertFalse(empty);
    }

    @Test
    public void containsTest() {
        var hasIt = FoldableFuns.contains(new SomeFoldable(), 4, List.of());
        assertFalse(hasIt);
        hasIt = FoldableFuns.contains(new SomeFoldable(), 2, List.of(1, 2, 3));
        assertTrue(hasIt);
        hasIt = FoldableFuns.contains(new SomeFoldable(), 5, List.of(1, 2, 3));
        assertFalse(hasIt);
    }

    @Test
    public void lengthTest() {
        var len = FoldableFuns.length(new SomeFoldable(), List.of());
        assertEquals(0, len);
        len = FoldableFuns.length(new SomeFoldable(), List.of(1, 2, 3));
        assertEquals(3, len);
    }

    @Test
    void filterTest() {
        var filtered = FoldableFuns.filter(new SomeFoldable(), FiniteListFunctional.getInstance(), FiniteListFunctional.getAlternativeMonoid(), List.of(1, 2, 3, 4), (Integer x) -> x % 2 == 0);
        assertEquals(FiniteList.of(2,4), filtered);
    }
}
