package com.dan323.algebraic;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.optional.Maybe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FiniteListMonoidTest {

    @Test
    public void operationTest() {
        var sol = FiniteListFunctional.getAlternativeMonoid().op(FiniteList.of(Maybe.of(), Maybe.of(true)), FiniteList.of(Maybe.of(false)));
        assertEquals(FiniteList.of(Maybe.of(), Maybe.of(true), Maybe.of(false)), sol);
    }

    @Test
    public void unitTest() {
        assertEquals(FiniteList.nil(), FiniteListFunctional.getAlternativeMonoid().unit());
    }
}
