package com.dan323.functional.data.either;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.funcs.IAlternative;

@Alternative
public final class RightAlternative<R> extends RightEither<R> implements IAlternative<Either<R, ?>> {

    private final IMonoid<R> rMonoid;

    public RightAlternative(IMonoid<R> rMonoid) {
        this.rMonoid = rMonoid;
    }

    public <A> Either<R, A> empty() {
        return Either.left(MonoidUtil.unit(rMonoid));
    }

    public <A> Either<R, A> disjunction(Either<R, A> first, Either<R, A> snd) {
        return first.either(x -> snd, Either::right);
    }

    @Override
    public Class<Either<R, ?>> getClassAtRuntime() {
        return (Class<Either<R, ?>>) (Class<?>) Either.class;
    }
}
