package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeFunctor;
import com.dan323.functional.data.pair.PairTypeContructor;
import com.dan323.functional.data.pair.ProdFunctor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProdTypesTest {

    @Test
    public void prodType() {
        var pair = new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(1, 2, 3), Maybe.of(5));
        assertEquals(FiniteList.of(1, 2, 3), pair.getFirst());
        assertEquals(Maybe.of(5), pair.getSecond());
    }

    @Test
    public void prodFunctor() {
        var pairFunctor = new ProdFunctor<>(FiniteListFunctional.getInstance(), MaybeFunctor.MAYBE_FUNCTOR);
        var pair = new PairTypeContructor<FiniteList<?>,Maybe<?>,Integer>(FiniteList.of(1, 2, 3), Maybe.of(5));
        var sol = pairFunctor.map(pair, (Integer x) -> x * 2);
        assertEquals(new PairTypeContructor<FiniteList<?>,Maybe<?>,Integer>(FiniteList.of(2,4,6), Maybe.of(10)),sol);
    }
}
