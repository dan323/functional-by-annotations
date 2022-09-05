package com.dan323.functional.data;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.BiFunction;
import java.util.function.Function;

import static com.dan323.functional.data.Identity.runIdentity;

@Monad
public class IdentityMonad implements IMonad<Identity<?>> {
    public static <B> Identity<B> pure(B b) {
        return new Identity<>(b);
    }

    public static <B, C> Identity<C> map(Identity<B> b, Function<B, C> function) {
        return pure(function.apply(runIdentity(b)));
    }

    public static <B, C> Identity<C> fapply(Identity<B> b, Identity<Function<B, C>> functionIdentity) {
        return map(b, runIdentity(functionIdentity));
    }

    public static <B> Identity<B> join(Identity<Identity<B>> b) {
        return runIdentity(b);
    }

    public static <B, C> Identity<C> flatMap(Function<B, Identity<C>> function, Identity<B> bIdentity) {
        return function.apply(runIdentity(bIdentity));
    }

    public static <B, C, D> Identity<D> liftA2(BiFunction<B, C, D> function, Identity<B> bIdentity, Identity<C> cIdentity) {
        return pure(function.apply(runIdentity(bIdentity), runIdentity(cIdentity)));
    }

    @Override
    public Class<Identity<?>> getClassAtRuntime() {
        return (Class<Identity<?>>) (Class) Identity.class;
    }
}
