package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.state.StackActions;
import org.junit.jupiter.api.Test;

import static com.dan323.functional.data.state.StackActions.StackError.poppingEmpty;
import static com.dan323.functional.data.state.StackActions.*;
import static org.junit.jupiter.api.Assertions.*;

public class StackActionsTest {

    @Test
    public void pushTest() {
        StackActions<Integer> stackActions = push(4).then(push(5)).then(push(7));
        assertEquals(Either.right(FiniteList.of(7, 5, 4)), stackActions.execute(FiniteList.nil()));
    }

    @Test
    public void dupTest() {
        StackActions<Integer> stackActions = push(4).then(dup());
        assertEquals(Either.right(FiniteList.of(4, 4)), stackActions.execute(FiniteList.nil()));
    }

    @Test
    public void popTest() {
        StackActions<Integer> stackActions = push(4).then(pop());
        assertEquals(Either.right(Maybe.of(4)), stackActions.evaluate(FiniteList.nil()));
        assertEquals(Either.right(Maybe.of(4)), stackActions.evaluate(FiniteList.of(1, 23, 13)));

        stackActions = pop();
        assertEquals(Either.left(poppingEmpty()), stackActions.evaluate(FiniteList.nil()));
        assertEquals(Either.left(poppingEmpty()), stackActions.execute(FiniteList.nil()));
        assertEquals(Either.right(Maybe.of(1)), stackActions.evaluate(FiniteList.of(1, 23, 13)));
        assertEquals(Either.right(FiniteList.of(23, 13)), stackActions.execute(FiniteList.of(1, 23, 13)));
    }

    @Test
    public void thenByPoppedTest() {
        StackActions<Integer> stackActions = push(4).then(push(6)).thenByPopped(x -> x.maybe(y -> y == 4 ? push(7) : push(9), push(8)));
        assertEquals(Either.right(FiniteList.of(8, 6, 4)), stackActions.execute(FiniteList.nil()));

        StackActions<Integer> stackActions2 = push(4).then(pop()).thenByPopped(x -> x.maybe(y -> y == 4 ? push(7) : push(9), push(8)));
        assertEquals(Either.right(FiniteList.of(7)), stackActions2.execute(FiniteList.nil()));
    }

    @Test
    public void doNothingTest() {
        StackActions<Integer> stackActions = push(4).then(push(6)).then(doNothing());
        assertEquals(Either.right(FiniteList.of(6, 4)), stackActions.execute(FiniteList.nil()));
        assertEquals(Either.right(Maybe.of()), stackActions.evaluate(FiniteList.nil()));

        StackActions<Integer> stackActions2 = push(4).then(drop());
        assertEquals(Either.right(FiniteList.of()), stackActions2.execute(FiniteList.nil()));
        assertEquals(Either.right(Maybe.of()), stackActions2.evaluate(FiniteList.nil()));
    }

    @Test
    public void overTest() {
        StackActions<Integer> stackActions = push(4).then(push(6)).then(over());
        assertEquals(Either.right(FiniteList.of(4, 6, 4)), stackActions.execute(FiniteList.nil()));
        assertEquals(Either.right(Maybe.of()), stackActions.evaluate(FiniteList.nil()));

        stackActions = push(4).then(over());
        assertEquals(Either.left(poppingEmpty()), stackActions.execute(FiniteList.nil()));
        assertEquals(Either.left(poppingEmpty()), stackActions.evaluate(FiniteList.nil()));

        stackActions = over();
        assertEquals(Either.left(poppingEmpty()), stackActions.execute(FiniteList.nil()));
        assertEquals(Either.left(poppingEmpty()), stackActions.evaluate(FiniteList.nil()));
    }


}
