package functional;

import functional.annotation.iface.FunctorUtils;
import functional.data.list.FiniteList;
import functional.data.list.zipper.ListZipper;
import functional.data.optional.Maybe;
import org.junit.jupiter.api.Test;

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

        lst = zipped.maybe(zip -> FunctorUtils.<ListZipper, ListZipper<?>,ListZipper<Boolean>, ListZipper<Boolean>, Boolean, Boolean>map(ListZipper.class, zip, b -> !b), null).toList();
        assertEquals(FiniteList.of(false, false, true), lst);
    }
}
