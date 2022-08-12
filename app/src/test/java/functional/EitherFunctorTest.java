package functional;

import functional.data.either.Either;
import functional.data.either.FLeftEither;
import functional.data.either.FRightEither;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EitherFunctorTest {

    @Test
    public void eitherLeft() {
        var left = Either.<Integer, Integer>left(5);
        var right = Either.<Integer, Integer>right(6);
        assertEquals(6, left.<Integer>either(k -> k + 1, k -> k - 1));
        assertEquals(5, right.<Integer>either(k -> k + 1, k -> k - 1));
    }

    @Test
    public void eitherFunctor() {
        var left = Either.<Integer, Integer>left(5);
        var right = Either.<Integer, Integer>right(6);

        var either = FRightEither.map(left, k -> k + 1);
        assertEquals(left, either);
        either = FRightEither.map(right, k -> k + 1);
        assertEquals(Either.right(7), either);
        either = FLeftEither.map(left, k -> k + 1);
        assertEquals(Either.left(6), either);
        either = FLeftEither.map(right, k -> k - 1);
        assertEquals(right, either);
    }
}
