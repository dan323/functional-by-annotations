package com.dan323.functional;

import com.dan323.functional.annotation.util.FunctorUtil;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.zipper.ListZipper;
import com.dan323.functional.data.optional.Maybe;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListZipperFunctorTest {
    @Test
    public void moveZipper() {
        Maybe<ListZipper<Boolean>> zipped = ListZipper.zipFrom(FiniteList.of(true, true, false));
        assertEquals(Maybe.of(), zipped.maybe(ListZipper::moveLeft, Maybe.of()));
        var right = zipped.<Maybe<ListZipper<Boolean>>>maybe(ListZipper::moveRight, Maybe.of());
        assertEquals(FiniteList.of(true, false, false), right.maybe(zip -> zip.set(false), null).toList());
        assertEquals(FiniteList.of(true, false, false), right.maybe(zip -> zip.modify(b -> !b), null).toList());
    }

    @Test
    public void maybeFunctorUtils() {
        Maybe<ListZipper<Boolean>> zipped = ListZipper.zipFrom(FiniteList.of(true, true, false));

        var lst = zipped.maybe(zip -> ListZipper.map(zip, b -> !b), null).toList();
        assertEquals(FiniteList.of(false, false, true), lst);

        lst = zipped.maybe(zip -> FunctorUtil.<ListZipper, ListZipper,ListZipper<Boolean>, ListZipper<Boolean>, Boolean, Boolean>map(ListZipper.zipFrom(FiniteList.of(true)).maybe(Function.identity(), null), ListZipper.class, zip, b -> !b), null).toList();
        assertEquals(FiniteList.of(false, false, true), lst);
    }
}
