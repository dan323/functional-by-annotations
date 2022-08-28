package com.dan323.functional.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.Optional;
import java.util.function.Function;

@Monad
public class MonadJoin implements IMonad<Optional<?>> {

    public <A> Optional<A> pure(A a) {
        return Optional.of(a);
    }

    public <A> Optional<A> join(Optional<Optional<A>> fa) {
        return fa.flatMap(Function.identity());
    }

    public <A, B> Optional<B> map(Optional<A> op, Function<A, B> fun) {
        return op.map(fun);
    }
}
