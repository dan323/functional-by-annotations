package com.dan323.functional.data.either;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public final class FLeftEither<B> implements IFunctor<Either<?, B>> {

    public static <A,C,B> Either<C,B> map(Either<A,B> base, Function<A,C> mapping){
        return base.either(b -> Either.left(mapping.apply(b)), Either::right);
    }

    /*
    @Override
    public <R> FLeftEither<R, B> pure(R r) {
        return new FLeftEither<>(Either.left(r));
    }

    @Override
    public <R> FLeftEither<R, B> join(Monad<Monad<R, FLeftEither<?, B>>, FLeftEither<?, B>> monadMonad) {
        var fleft = ((FLeftEither<FLeftEither<R, B>, B>) (Monad<?, ?>) monadMonad);
        return fleft.toEither().either(Function.identity(), b -> new FLeftEither<>(Either.right(b)));
    }

    @Override
    public <R> FLeftEither<R, B> map(Function<A, R> f) {
        Function<A, Either<R, B>> g = a -> Either.left(f.apply(a));
        return new FLeftEither<>(either.either(g, Either::right));
    }

    public Either<A, B> toEither() {
        return either;
    }

    @Override
    public <R> FLeftEither<R, B> flatMap(Function<A, Monad<R, FLeftEither<?, B>>> f) {
        var g = (Function<A,FLeftEither<R,B>>)(Function<?,?>)f;
        return either.either(g,b -> new FLeftEither<>(Either.right(b)));
    }*/
}
