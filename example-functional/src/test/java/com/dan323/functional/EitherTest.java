package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.either.LeftEither;
import com.dan323.functional.data.either.RightEither;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EitherTest {

    @Test
    public void eitherConstructor() {
        var left = Either.<Integer, Integer>left(5);
        var right = Either.<Integer, Integer>right(6);
        assertEquals(6, left.<Integer>either(k -> k + 1, k -> k - 1));
        assertEquals(5, right.<Integer>either(k -> k + 1, k -> k - 1));
    }

    @Test
    public void eitherFunctor() {
        var left = Either.<Integer, Integer>left(5);
        var right = Either.<Integer, Integer>right(6);

        var either = RightEither.<Integer>getInstance().map(left, k -> k + 1);
        assertEquals(left, either);
        either = RightEither.<Integer>getInstance().map(right, k -> k + 1);
        assertEquals(Either.right(7), either);
        either = LeftEither.<Integer>getInstance().map(left, k -> k + 1);
        assertEquals(Either.left(6), either);
        either = LeftEither.<Integer>getInstance().map(right, k -> k - 1);
        assertEquals(right, either);
    }

    @Test
    public void eitherPure() {
        var left = Either.<Integer, Integer>left(5);
        var right = Either.<Integer, Integer>right(6);

        var either = RightEither.<Integer>getInstance().pure(6);
        assertEquals(right, either);
        either = LeftEither.<Integer>getInstance().pure(5);
        assertEquals(left, either);
    }

    @Test
    public void eitherFapply() {
        var left = Either.<Integer, Integer>left(5);
        var right = Either.<Integer, Integer>right(6);
        var leftL = Either.<Integer, Function<Integer, Integer>>left(5);
        var rightL = Either.<Integer, Function<Integer, Integer>>right(x -> x * 2);
        var leftR = Either.<Function<Integer, Integer>, Integer>left(x -> x * 2);
        var rightR = Either.<Function<Integer, Integer>, Integer>right(6);

        var either = RightEither.<Integer>getInstance().fapply(leftL, right);
        assertEquals(left, either);

        either = RightEither.<Integer>getInstance().fapply(leftL, left);
        assertEquals(left, either);

        either = RightEither.<Integer>getInstance().fapply(rightL, left);
        assertEquals(left, either);

        either = RightEither.<Integer>getInstance().fapply(rightL, right);
        assertEquals(Either.right(12), either);

        either = LeftEither.<Integer>getInstance().fapply(leftR, right);
        assertEquals(right, either);

        either = LeftEither.<Integer>getInstance().fapply(leftR, left);
        assertEquals(Either.left(10), either);

        either = LeftEither.<Integer>getInstance().fapply(rightR, left);
        assertEquals(right, either);

        either = LeftEither.<Integer>getInstance().fapply(rightR, right);
        assertEquals(right, either);
    }

    @Test
    public void eitherFlatMap() {
        var left = Either.<Integer, Integer>left(5);
        var right = Either.<Integer, Integer>right(6);

        var either = LeftEither.<Integer>getInstance().flatMap(x -> Either.left(x * 2), left);
        assertEquals(Either.left(10), either);

        either = LeftEither.<Integer>getInstance().flatMap(Either::right, left);
        assertEquals(Either.right(5), either);

        either = LeftEither.<Integer>getInstance().flatMap(x -> Either.left(x * 4), right);
        assertEquals(right, either);

        either = RightEither.<Integer>getInstance().flatMap(x -> Either.right(x * 2), right);
        assertEquals(Either.right(12), either);

        either = RightEither.<Integer>getInstance().flatMap(Either::left, right);
        assertEquals(Either.left(6), either);

        either = RightEither.<Integer>getInstance().flatMap(Either::right, left);
        assertEquals(left, either);
    }

    @Test
    public void eitherJoin(){
        var leftL = Either.<Either<Integer,String>, String>left(Either.left(5));
        var leftRightL = Either.<Either<Integer,String>,String>left(Either.right("7"));
        var rightL = Either.<Either<Integer,String>, String>right("6");

        assertEquals( Either.left(5) , LeftEither.<String>getInstance().join(leftL));
        assertEquals(Either.right("6"), LeftEither.<String>getInstance().join(rightL));
        assertEquals(Either.right("7"), LeftEither.<String>getInstance().join(leftRightL));


        var leftR = Either.<Integer, Either<Integer,String>>left(5);
        var rightLeftR = Either.<Integer, Either<Integer,String>>right(Either.left(7));
        var rightR = Either.<Integer, Either<Integer,String>>right(Either.right("0"));

        assertEquals( Either.left(5) , RightEither.<Integer>getInstance().join(leftR));
        assertEquals(Either.right("0"), RightEither.<Integer>getInstance().join(rightR));
        assertEquals(Either.left(7), RightEither.<Integer>getInstance().join(rightLeftR));
    }
}
