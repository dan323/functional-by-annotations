package com.dan323.functional.data.state;

import com.dan323.functional.data.Void;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.pair.Pair;

import java.util.function.Function;

/**
 * State without errors
 *
 * @param <A> output type
 * @param <S> state type
 */
@FunctionalInterface
public interface State<A, S> extends StateWithError<A, S, Void> {

    static <A,S> State<A,S> fromFun(Function<S,Pair<A,S>> fun){
        return s -> Either.right(fun.apply(s));
    }


}
