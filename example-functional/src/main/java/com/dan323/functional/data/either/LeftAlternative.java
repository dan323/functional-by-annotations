package com.dan323.functional.data.either;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.funcs.IAlternative;

@Alternative
public final class LeftAlternative<R> extends LeftEither<R> implements IAlternative<Either<?, R>> {

    private final IMonoid<R> rMonoid;

    public LeftAlternative(IMonoid<R> rMonoid) {
        this.rMonoid = rMonoid;
    }

    public <A> Either<A, R> empty() {
        return Either.right(MonoidUtil.unit(rMonoid));
    }

    public <A> Either<A, R> disjunction(Either<A, R> first, Either<A, R> snd) {
        return first.either(Either::left, x -> snd);
    }

    @Override
    public Class<Either<?, R>> getClassAtRuntime() {
        return (Class<Either<?, R>>) (Class<?>) Either.class;
    }
}
