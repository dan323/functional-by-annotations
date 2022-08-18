package com.dan323.functional.data.either;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.util.function.Function;

@Functor
public final class FRightEither<A> implements IFunctor<Either<A, ?>> {

    public static <B,C,A> Either<A,C> map(Either<A,B> base, Function<B,C> mapping){
        return base.either(Either::left, b -> Either.right(mapping.apply(b)));
    }
    /*
    @Override
    public <R> FRightEither<A, R> pure(R r) {
        return new FRightEither<>(Either.right(r));
    }

    @Override
    public <R> FRightEither<A, R> join(Monad<Monad<R, FRightEither<A, ?>>, FRightEither<A, ?>> monadMonad) {
        var g = (FRightEither<A,FRightEither<A,R>>)(Monad<?,?>)monadMonad;
        return g.either.either(a -> new FRightEither<>(Either.left(a)), Function.identity());
    }

    @Override
    public <R> FRightEither<A, R> map(Function<B, R> f) {
        Function<B, Either<A, R>> g = a -> Either.right(f.apply(a));
        return new FRightEither<>(either.either(Either::left, g));
    }

    @Override
    public <R> FRightEither<A, R> flatMap(Function<B, Monad<R, FRightEither<A, ?>>> f) {
        var g = (Function<B,FRightEither<A,R>>)(Function<?,?>)f;


    }

    @Override
    public <Q> FRightEither<A,Q> fapply(Applicative<Function<B, Q>, FRightEither<A, ?>> ff) {
        return Monad.super.fapply(ff);
    }
    */
}
