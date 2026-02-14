package com.dan323.functional.data.state;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.function.FunctionFrom;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;
import com.dan323.functional.data.pair.Pair;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Actions to take in a stack, represented as a finite list {@link FiniteList<A>}
 *
 * @param <A> type of elements in the stack
 */
public interface StackActions<A> extends StateWithError<Maybe<A>, FiniteList<A>, StackActions.StackError> {

    class StackError {

        private final String message;

        private StackError(String message) {
            this.message = message;
        }

        public static StackError poppingEmpty() {
            return new StackError("Cannot perform this action on an empty stack");
        }

        public static StackError illegalState() {
            return new StackError("This state should be unreachable");
        }

        @Override
        public String toString() {
            return "[ERROR]: " + message;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof StackError stackError) && Objects.equals(stackError.message, message);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(message);
        }
    }

    // PURE BASIC ACTIONS

    /**
     * Add an element to the stack
     *
     * @param x   element to add
     * @param <A> type of elements in the stack
     * @return ... -> ... x |
     */
    static <A> StackActions<A> push(A x) {
        return s -> Either.right(new Pair<>(Maybe.of(), FiniteList.cons(x, s)));
    }

    /**
     * Extract the top most element
     *
     * @param <A> type of elements in the stack
     * @return ... x -> ... | x
     */
    static <A> StackActions<A> pop() {
        return s -> s.head().maybe(h -> Either.right(new Pair<>(Maybe.of(h), s.tail())), Either.left(StackError.poppingEmpty()));
    }

    /**
     * <p>
     * Reset the state to be empty.
     * </p><p>
     * TO BE USED ONLY FOR TESTING PURPOSES
     * </p>
     *
     * @param <A> type of elements in the stack
     * @return ... -> |
     */
    static <A> StackActions<A> reset() {
        return s -> Either.right(new Pair<>(Maybe.of(), FiniteList.nil()));
    }

    /**
     * Modify the topmost element
     *
     * @param modif function to apply
     * @param <A>   type of elements in the stack
     * @return ... x -> ... modif(x)
     */
    static <A> StackActions<A> modify(UnaryOperator<A> modif) {
        return StackActions.<A>pop().thenByPopped(m -> MaybeMonad.map(m, modif).maybe(StackActions::push, error(StackError.illegalState())));
    }

    /**
     * stack that fails
     *
     * @param stackError error to fail with
     * @param <A>        type of elements in the stack
     * @return ... -> ERROR
     */
    static <A> StackActions<A> error(StackError stackError) {
        return s -> Either.left(stackError);
    }

    /**
     * Remove the top most element
     *
     * @param <A> type of elements in the stack
     * @return ... x -> ... |
     */
    static <A> StackActions<A> drop() {
        return StackActions.<A>pop().then(doNothing());
    }

    /**
     * Leave the state as is, no output
     *
     * @param <A> type of elements in the stack
     * @return ... -> ... |
     */
    static <A> StackActions<A> doNothing() {
        return s -> Either.right(new Pair<>(Maybe.of(), s));
    }

    /**
     * Duplicate the top most element
     *
     * @param <A> type of the elements in the stack
     * @return ... x -> ... x x |
     */
    static <A> StackActions<A> dup() {
        return StackActions.<A>pop().thenByPopped(m -> m.maybe(x -> push(x).then(push(x)), error(StackError.illegalState())));
    }

    /**
     * Interchange the 2 topmost elements in the stack
     *
     * @param <A> type of elements in the stack
     * @return ... x y -> ... y x |
     */
    static <A> StackActions<A> swap() {
        return StackActions.<A>pop().thenByPopped(m -> m.maybe(x -> StackActions.<A>pop().thenByPopped(n -> n.maybe(h -> StackActions.<A>push(x).then(push(h)), error(StackError.illegalState()))), error(StackError.illegalState())));
    }

    /**
     * Copy the over the topmost element in the stack
     *
     * @param <A> type of elements in the stack
     * @return ... x y -> ... x y x |
     */
    static <A> StackActions<A> over() {
        return StackActions.<A>pop().thenByPopped(m -> m.maybe(x -> StackActions.<A>pop().thenByPopped(n -> n.maybe(h -> StackActions.<A>push(h).then(push(x)).then(push(h)), error(StackError.illegalState()))), error(StackError.illegalState())));
    }

    // CONCATENATIONS

    /**
     * Concatenate a different stack action depending on the previous output
     *
     * @param fun dependency
     * @return concatenated stack actions first the current one and after, fun applied to the output of this one
     * @see #then(StackActions)
     */
    default StackActions<A> thenByPopped(Function<Maybe<A>, StackActions<A>> fun) {
        Function<Maybe<A>, StateWithError<Maybe<A>, FiniteList<A>, StackError>> ff = fun::apply;
        return s -> StateMonad.<FiniteList<A>, StackError>getInstance().flatMap(ff, this).apply(s);
    }

    /**
     * Concatenate stack actions
     *
     * @param st next action
     * @return a stack action that performs them in sequence
     * @see #thenByPopped(Function)
     */
    default StackActions<A> then(StackActions<A> st) {
        return this.thenByPopped(FunctionFrom.<Maybe<A>>getInstance().pure(st));
    }

    /**
     * Print the final pair {@link Maybe<A>} and {@link List<A>} of the stack starting with an empty list
     *
     * @return the left error or the right pair result to apply this state to an empty list
     * @see Pair#toString()
     * @see com.dan323.functional.data.either.Left#toString()
     * @see com.dan323.functional.data.either.Right#toString()
     */
    default String print() {
        return apply(FiniteList.nil()).toString();
    }

}
