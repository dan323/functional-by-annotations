package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.stack.Stack;
import org.junit.jupiter.api.Test;

import static com.dan323.functional.data.stack.Stack.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StackTest {

    @Test
    public void pushTest() {
        Stack<Integer> stack = push(4).then(push(5)).then(push(7));
        assertEquals(FiniteList.of(7, 5, 4), stack.execute(FiniteList.nil()));
    }

    @Test
    public void dupTest() {
        Stack<Integer> stack = push(4).then(dup());
        assertEquals(FiniteList.of(4, 4), stack.execute(FiniteList.nil()));
    }

    @Test
    public void popTest() {
        Stack<Integer> stack = push(4).then(pop());

        assertEquals(Maybe.of(4), stack.evaluate(FiniteList.nil()));
        assertEquals(Maybe.of(4), stack.evaluate(FiniteList.of(1, 23, 13)));
    }

    @Test
    public void thenByPoppedTest() {
        Stack<Integer> stack = push(4).then(push(6)).thenByPopped(x -> x.maybe(y -> y == 4 ? push(7) : push(9), push(8)));
        assertEquals(FiniteList.of(8, 6, 4), stack.execute(FiniteList.nil()));

        Stack<Integer> stack2 = push(4).then(pop()).thenByPopped(x -> x.maybe(y -> y == 4 ? push(7) : push(9), push(8)));
        assertEquals(FiniteList.of(7), stack2.execute(FiniteList.nil()));
    }

    @Test
    public void doNothingTest() {
        Stack<Integer> stack = push(4).then(push(6)).then(doNothing());
        assertEquals(FiniteList.of(6, 4), stack.execute(FiniteList.nil()));
        assertEquals(Maybe.of(), stack.evaluate(FiniteList.nil()));

        Stack<Integer> stack2 = push(4).then(pop()).then(doNothing());
        assertEquals(FiniteList.of(), stack2.execute(FiniteList.nil()));
        assertEquals(Maybe.of(), stack2.evaluate(FiniteList.nil()));
    }

}
