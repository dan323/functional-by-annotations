package com.dan323.functional.annotation.compiler.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.util.Identity;

import java.util.Optional;
import java.util.function.Function;

@Functor
public class DoubleFunctor implements IFunctor<Optional<Identity<?>>> {
    @Override
    public Class<Optional<Identity<?>>> getClassAtRuntime() {
        return (Class<Optional<Identity<?>>>) (Class<?>) Optional.class;
    }

    public <A, B> Optional<Identity<B>> map(Optional<Identity<A>> base, Function<A, B> function) {
        return base.map(id -> new Identity<>(function.apply(id.get())));
    }
}
