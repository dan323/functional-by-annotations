package com.dan323.functional.data;

import com.dan323.functional.data.list.List;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.state.State;

public final class Stack<A> implements State<Maybe<A>, List<A>> {

    private final State<Maybe<A>, List<A>> state;

    public Stack(State<Maybe<A>, List<A>> state) {
        this.state = state;
    }

    public static <A> Stack<A> push(A x) {
        return new Stack<>(s -> new Pair<>(Maybe.of(), List.cons(x, s)));
    }

    public static <A> Stack<A> pop() {
        return new Stack<>(s -> new Pair<>(s.head(), s.tail()));
    }

    /**
     * Print the final pair {@link Maybe<A>} and {@link List<A>} of the stack starting with an empty list
     *
     * @return {@link Pair#toString()} applied to the pair resulting from an empty list
     */
    public String toString() {
        return state.apply(List.nil()).toString();
    }

    @Override
    public Pair<Maybe<A>, List<A>> apply(List<A> aList) {
        return state.apply(aList);
    }
}
