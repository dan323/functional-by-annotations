package com.dan323.functional.data.either;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.Function;

@Monad
public final class LeftEither<R> implements IMonad<Either<?, R>> {

    private LeftEither(){}
    public static <R> LeftEither<R> getInstance(){
        return new LeftEither<>();
    }
    public <A, B> Either<B, R> map(Either<A, R> base, Function<A, B> mapping) {
        return base.either(((Function<B, Either<B, R>>) Either::left).compose(mapping), Either::right);
    }

    public <B> Either<B, R> pure(B r) {
        return Either.left(r);
    }

    public <A, B> Either<B, R> flatMap(Function<A, Either<B, R>> f, Either<A, R> base) {
        return base.either(f, Either::right);
    }

    public <A, B> Either<B, R> fapply(Either<Function<A, B>, R> fEither, Either<A, R> base) {
        return fEither.either(f -> map(base, f), Either::right);
    }

    public <A> Either<A, R> join(Either<Either<A, R>, R> eitherEither) {
        return eitherEither.either(Function.identity(), Either::right);
    }
}
