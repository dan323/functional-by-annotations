package com.dan323.functional;

import com.dan323.Stack;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.state.StateMonad;
import org.junit.jupiter.api.Test;

import static com.dan323.Stack.pop;
import static com.dan323.Stack.push;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListTest {

    @Test
    public void nilTail() {
        assertEquals(List.nil(), List.nil().tail());
        assertEquals(List.nil(), FiniteList.of());
    }

    @Test
    public void consToFiniteList() {
        var sol = List.cons(5, FiniteList.of(1, 2));
        assertEquals(FiniteList.of(5, 1, 2), sol);
    }
}
