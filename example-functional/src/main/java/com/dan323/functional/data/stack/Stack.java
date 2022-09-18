package com.dan323.functional.data.stack;

import com.dan323.functional.data.function.FunctionFrom;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.state.State;
import com.dan323.functional.data.state.StateMonad;

import java.util.function.Function;

public final class Stack<A> implements State<Maybe<A>, FiniteList<A>> {

    private final State<Maybe<A>, FiniteList<A>> state;

    private Stack(State<Maybe<A>, FiniteList<A>> state) {
        this.state = state;
    }

    public static <A> Stack<A> push(A x) {
        return new Stack<>(s -> new Pair<>(Maybe.of(), FiniteList.cons(x, s)));
    }

    public static <A> Stack<A> pop() {
        return new Stack<>(s -> new Pair<>(s.head(), s.tail()));
    }

    public static <A> Stack<A> empty() {
        return new Stack<>(s -> new Pair<>(Maybe.of(), FiniteList.nil()));
    }

    public static <A> Stack<A> doNothing() {
        return new Stack<>(s -> new Pair<>(Maybe.of(), s));
    }

    public static <A> Stack<A> dup() {
        return Stack.<A>pop().thenByPopped(m -> m.maybe(x -> push(x).then(push(x)), empty()));
    }

    public static <A> Stack<A> over() {
        return Stack.<A>pop().thenByPopped(m -> m.maybe(x -> Stack.<A>pop().thenByPopped(n -> n.maybe(h -> Stack.<A>push(x).then(push(h)), push(x))), empty()));
    }

    public Stack<A> thenByPopped(Function<Maybe<A>, Stack<A>> fun) {
        Function<Maybe<A>, State<Maybe<A>, FiniteList<A>>> ff = fun::apply;
        return new Stack<>(StateMonad.<FiniteList<A>>getInstance().flatMap(ff, this));
    }

    public Stack<A> then(Stack<A> st) {
        return this.thenByPopped(FunctionFrom.<Maybe<A>>getInstance().pure(st));
    }

    /**
     * Print the final pair {@link Maybe<A>} and {@link List<A>} of the stack starting with an empty list
     *
     * @return {@link Pair#toString()} applied to the pair resulting from an empty list
     */
    public String toString() {
        return state.apply(FiniteList.nil()).toString();
    }

    @Override
    public Pair<Maybe<A>, FiniteList<A>> apply(FiniteList<A> aList) {
        return state.apply(aList);
    }
}
