package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.zipper.ListZipper;
import com.dan323.functional.data.optional.Maybe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListZipperTest {
    @Test
    public void testZipFromAndToList() {
        var list = FiniteList.of(1, 2, 3);
        var zipper = ListZipper.zipFrom(list);
        assertEquals(list, zipper.toList());
    }

    @Test
    public void testGetAndSet() {
        var zipper = ListZipper.zipFrom(FiniteList.of(10, 20, 30));
        assertEquals(Maybe.of(10), zipper.get());
        var modified = zipper.set(99);
        assertEquals(Maybe.of(99), modified.get());
        assertEquals(FiniteList.of(99, 20, 30), modified.toList());
    }

    @Test
    public void testMoveRightAndLeft() {
        var zipper = ListZipper.zipFrom(FiniteList.of(1, 2, 3));
        var right = zipper.moveRight();
        // right is Maybe<ListZipper<Integer>>
        assertTrue(right.maybe(z -> z.get().maybe(v -> v == 2, false), false));
        var left = right.<Maybe<ListZipper<Integer>>>maybe(ListZipper::moveLeft, Maybe.of());
        assertTrue(left.maybe(z -> z.get().maybe(v -> v == 1, false), false));
    }

    @Test
    public void testModify() {
        var zipper = ListZipper.zipFrom(FiniteList.of(5, 6, 7));
        var modified = zipper.modify(x -> x * 2);
        assertEquals(Maybe.of(10), modified.get());
        assertEquals(FiniteList.of(10, 6, 7), modified.toList());
    }

    @Test
    public void testIndex() {
        var zipper = ListZipper.zipFrom(FiniteList.of(8, 9));
        assertEquals(0, zipper.index());
        var moved = zipper.moveRight().maybe(z -> z, null);
        assertNotNull(moved);
        assertEquals(1, moved.index());
    }
}
