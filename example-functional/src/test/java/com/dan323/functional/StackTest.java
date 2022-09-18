package com.dan323.functional;

import com.dan323.Stack;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.state.StateMonad;
import org.junit.jupiter.api.Test;

import static com.dan323.Stack.pop;
import static com.dan323.Stack.push;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackTest {

    @Test
    public void test() {
        StateMonad<List<Integer>> stateMonad = new StateMonad<>();
        Stack<Integer> stack = push(4);
        stack = new Stack<>(stateMonad.flatMap(x -> push(6), stack));
        stack = new Stack<>(stateMonad.flatMap(x -> pop(), stack));
        assertEquals(Maybe.of(6), stack.evaluate(List.nil()));
        var stack2 = new Stack<>(stateMonad.flatMap(x -> x.maybe(y -> y == 4 ? push(7) : push(9), push(8)), stack));
        assertEquals(FiniteList.of(9, 4), stack2.execute(List.nil()));
        var stack3 = new Stack<>(stateMonad.flatMap(x -> x.maybe(y -> y == 6 ? push(7) : push(9), push(8)), stack));
        assertEquals(FiniteList.of(7, 4), stack3.execute(List.nil()));
        assertEquals(Maybe.of(), stack3.evaluate(List.nil()));
    }

}
