package com.dan323.functional.data.list;

import java.util.function.Function;

/**
 * Interface for infinite lists, lists implementing this interface should compute the elements lazily (only when queried)
 *
 * @param <A> type of elements in the list
 */
public sealed interface InfiniteList<A> extends List<A> permits Cons, Generating, Generating.GeneratingMapped, Repeat, Zipped {

    @Override
    InfiniteList<A> tail();

    @Override
    <B> InfiniteList<B> map(Function<A, B> mapping);

}
