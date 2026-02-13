package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FiniteListTest {

    @Test
    public void emptyListMap() {
        var sol = FiniteListFunctional.map(FiniteList.nil(), Object::toString);
        assertEquals(FiniteList.nil(), sol);
    }

    @Test
    public void nonEmptyListMap() {
        var sol = FiniteListFunctional.map(FiniteList.of(1, 2, 3), x -> x * 2);
        assertEquals(FiniteList.of(2, 4, 6), sol);
    }

    @Test
    public void listPure() {
        var sol = FiniteListFunctional.pure(5);
        assertEquals(FiniteList.of(5), sol);
    }

    @Test
    public void listFlatMap() {
        var sol = FiniteListFunctional.flatMap(x -> FiniteList.of(x, x), FiniteList.of(1, 2, 3));
        assertEquals(FiniteList.of(1, 1, 2, 2, 3, 3), sol);
    }

    @Test
    public void fromJavaListEmpty() {
        var javaList = java.util.List.of();
        var finiteList = FiniteList.fromJavaList(javaList);
        assertEquals(FiniteList.nil(), finiteList);
    }

    @Test
    public void fromJavaListSingleElement() {
        var javaList = java.util.List.of(42);
        var finiteList = FiniteList.fromJavaList(javaList);
        assertEquals(FiniteList.of(42), finiteList);
    }

    @Test
    public void fromJavaListMultipleElements() {
        var javaList = java.util.List.of(1, 2, 3, 4, 5);
        var finiteList = FiniteList.fromJavaList(javaList);
        assertEquals(FiniteList.of(1, 2, 3, 4, 5), finiteList);
    }

    @Test
    public void fromJavaListPreservesOrder() {
        var javaList = java.util.List.of("a", "b", "c");
        var finiteList = FiniteList.fromJavaList(javaList);
        assertEquals(FiniteList.of("a", "b", "c"), finiteList);
    }
}
