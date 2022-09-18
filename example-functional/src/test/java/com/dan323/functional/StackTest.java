package com.dan323.functional;

import com.dan323.functional.data.stack.Stack;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.state.StateMonad;
import org.junit.jupiter.api.Test;

import static com.dan323.functional.data.stack.Stack.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackTest {

    @Test
    public void test() {
        StateMonad<List<Integer>> stateMonad = StateMonad.getInstance();
        Stack<Integer> stack = push(4);
        stack = stack.map(push(6));
        stack = stack.map(pop());
        assertEquals(Maybe.of(6), stack.evaluate(FiniteList.nil()));
        stack = stack.map(dup());

        var stack2 = stack.flatMap(x -> x.maybe(y -> y == 4 ? push(7) : push(9), push(8)));
        assertEquals(FiniteList.of(8, 4, 4), stack2.execute(FiniteList.nil()));

        var stack3 = stack.map(pop());
        stack3 = stack3.flatMap(x -> x.maybe(y -> y == 6 ? push(7) : push(9), push(8)));
        assertEquals(FiniteList.of(9, 4), stack3.execute(FiniteList.nil()));
        assertEquals(Maybe.of(), stack3.evaluate(FiniteList.nil()));

        var stack4 = stack.map(pop());
        stack4 = stack4.flatMap(x -> x.maybe(y -> y == 4 ? push(7) : push(9), push(8)));
        assertEquals(FiniteList.of(7, 4), stack4.execute(FiniteList.nil()));
        assertEquals(Maybe.of(), stack4.evaluate(FiniteList.nil()));
    }

}
