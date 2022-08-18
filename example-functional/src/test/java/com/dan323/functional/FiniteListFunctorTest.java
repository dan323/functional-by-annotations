package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FiniteListFunctorTest {

    @Test
    public void emptyListMap() {
        var sol = FiniteListFunctor.map(FiniteList.nil(), Object::toString);
        assertEquals(FiniteList.nil(), sol);
    }

    @Test
    public void nonEmptyListMap() {
        var sol = FiniteListFunctor.map(FiniteList.of(1, 2, 3), x -> x * 2);
        assertEquals(FiniteList.of(2, 4, 6), sol);
    }
}
