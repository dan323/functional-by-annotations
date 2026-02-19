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
    public void testMoveRightBeyondLastReturnsEmptyMaybe() {
        var zipper = ListZipper.zipFrom(FiniteList.of(1, 2, 3));
        // Move to the second element
        var second = zipper.moveRight();
        // Move to the third (last) element
        var third = second.<Maybe<ListZipper<Integer>>>maybe(ListZipper::moveRight, Maybe.of());
        // Moving right from the last element should yield an empty Maybe
        var beyondLast = third.<Maybe<ListZipper<Integer>>>maybe(ListZipper::moveRight, Maybe.of());
        assertEquals(beyondLast.maybe(ListZipper::get, Maybe.of(5)), Maybe.of());
    }

    @Test
    public void testMoveLeftBeyondFirstReturnsEmptyMaybe() {
        var zipper = ListZipper.zipFrom(FiniteList.of(1, 2, 3));
        // Moving left from the first element should yield an empty Maybe
        var beforeFirst = zipper.moveLeft();
        assertEquals(Maybe.of(), beforeFirst);
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
        assertTrue(zipper.moveRight().maybe(z -> z.index() == 1, false));
    }

    @Test
    public void testEmptyListZipper() {
        var zipper = ListZipper.zipFrom(FiniteList.nil());
        assertEquals(Maybe.of(), zipper.get());
        assertEquals(Maybe.of(), zipper.moveRight());
        assertEquals(Maybe.of(), zipper.moveLeft());
    }
}
