package com.dan323.functional;

import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.compiler.util.MonadUtil;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.list.zipper.ListZipper;
import com.dan323.functional.data.list.zipper.ListZipperFunctor;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListZipperFunctorTest {
    @Test
    public void moveZipper() {
        ListZipper<Boolean> zipped = ListZipper.zipFrom(FiniteList.of(true, true, false));
        assertEquals(Maybe.of(), zipped.moveLeft());
        var maybeZip = MonadUtil.flatMap(MaybeMonad.getInstance(), (ListZipper<Boolean> x) -> x.moveLeft(), zipped.moveRight());
        assertEquals(Maybe.of(true), MonadUtil.flatMap(MaybeMonad.getInstance(), (ListZipper<Boolean> lz) -> lz.get(), maybeZip));
        var right = zipped.moveRight();
        assertEquals(FiniteList.of(true, false, false), right.maybe(zip -> zip.set(false), null).toList());
        assertEquals(FiniteList.of(true, false, false), right.maybe(zip -> zip.modify(b -> !b), null).toList());
    }

    @Test
    public void maybeFunctorUtils() {
        ListZipper<Boolean> zipped = ListZipper.zipFrom(FiniteList.of(true, true, false));

        var lst = ListZipperFunctor.map(zipped, b -> !b).toList();
        assertEquals(FiniteList.of(false, false, true), lst);

        lst = (List<Boolean>) FunctorUtil.map(ListZipperFunctor.getInstance(), zipped, (Boolean b) -> !b).toList();
        assertEquals(FiniteList.of(false, false, true), lst);
    }
}
