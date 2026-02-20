package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTest {

    @Test
    public void nilTail() {
        assertEquals(List.nil(), List.nil().tail());
        assertEquals(List.nil(), FiniteList.of());
        assertEquals(0, FiniteList.nil().length());
    }

    @Test
    public void consToFiniteList() {
        var sol = FiniteList.of(1, 2).cons(5);
        assertEquals(FiniteList.of(5, 1, 2), sol);
    }
}
