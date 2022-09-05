package com.dan323.functional.util.functor;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Functor
public class FunctorMapConst implements IFunctor<Optional<?>> {

    @Override
    public Class<Optional<?>> getClassAtRuntime() {
        return (Class<Optional<?>>)(Class) Optional.class;
    }

    public <A, B> Optional<B> map(Optional<A> base, Function<A, B> fun) {
        return base.map(fun);
    }

    public <A, B> Optional<B> mapConst(Optional<A> base, B constant) {
        return Optional.empty();
    }
}
