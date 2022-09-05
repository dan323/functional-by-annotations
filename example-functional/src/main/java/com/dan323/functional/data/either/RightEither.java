package com.dan323.functional.data.either;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.Function;

@Monad
public final class RightEither<R> implements IMonad<Either<R, ?>> {

    private RightEither(){
    }

    public static <R> RightEither<R> getInstance(){
        return new RightEither<>();
    }
    public <B, C> Either<R, C> map(Either<R, B> base, Function<B, C> mapping) {
        return base.either(Either::left, b -> Either.right(mapping.apply(b)));
    }

    public <A> Either<R, A> pure(A r) {
        return Either.right(r);
    }

    public <A> Either<R,A> join(Either<R,Either<R,A>> eitherEither) {
        return eitherEither.either(Either::left,Function.identity());
    }

    public <A,B> Either<R, B> flatMap(Function<A, Either<R,B>> f, Either<R,A> base) {
        return base.either(Either::left,f);
    }

    public <A,B> Either<R,B> fapply(Either<R,Function<A,B>> ff, Either<R,A> base) {
        return ff.either(Either::left, f -> map(base,f));
    }

    @Override
    public Class<Either<R,?>> getClassAtRuntime() {
        return (Class<Either<R,?>>)(Class) Either.class;
    }
}
