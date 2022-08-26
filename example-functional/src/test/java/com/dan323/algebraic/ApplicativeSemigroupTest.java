package com.dan323.algebraic;

import com.dan323.functional.data.bool.AndMonoid;
import com.dan323.functional.data.bool.OrMonoid;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.either.LeftEither;
import com.dan323.functional.data.util.applicative.ApplicativeMonoid;
import com.dan323.functional.data.util.applicative.ApplicativeSemigroup;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicativeSemigroupTest {
    @Test
    public void applicativeOnSemigroup() {
        var semigroup = new ApplicativeSemigroup<Either<Boolean, Integer>, Either<?, Integer>, Boolean>(AndMonoid.AND_MONOID, LeftEither.getInstance());
        var op = semigroup.op(Either.left(true), Either.left(false));
        assertEquals(Either.left(false), op);
        op = semigroup.op(Either.left(true), Either.left(true));
        assertEquals(Either.left(true), op);
        op = semigroup.op(Either.left(true), Either.right(8));
        assertEquals(Either.right(8), op);
        op = semigroup.op(Either.right(9), Either.right(8));
        assertEquals(Either.right(9), op);
    }

    @Test
    public void applicativeOnMonoid() {
        var monoid = new ApplicativeMonoid<Either<Boolean,Integer>, Either<?,Integer>, Boolean>(OrMonoid.OR_MONOID, LeftEither.getInstance());
        var op = monoid.op(Either.left(true), Either.left(false));
        assertEquals(Either.left(true), op);
        op = monoid.op(Either.left(true), Either.left(true));
        assertEquals(Either.left(true), op);
        op = monoid.op(Either.left(true), Either.right(8));
        assertEquals(Either.right(8), op);
        op = monoid.op(Either.right(9), Either.right(8));
        assertEquals(Either.right(9), op);
        assertEquals(Either.left(false), monoid.unit());
    }
}
