package com.dan323.mock;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;
import java.util.function.Function;

@Semigroup
@Monad
public class SomeMonad implements ISemigroup<Integer>, IMonad<List<?>> {

    public Integer op(Integer a, Integer b) {
        return (a + b) % 5;
    }

    public <A> List<A> pure(A a) {
        return List.of(a);
    }

    public <A, B> List<B> flatMap(Function<A, List<B>> fun, List<A> base) {
        return base.stream().map(fun).flatMap(List::stream).toList();
    }
}
