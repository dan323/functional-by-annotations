package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.either.LeftAlternative;
import com.dan323.functional.data.integer.SumMonoid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EitherAlternativeTest {

    @Test
    public void alternativeLeft() {
        LeftAlternative<Integer> leftAlternative = new LeftAlternative<>(SumMonoid.getInstance());

        Either<Boolean, Integer> either = leftAlternative.empty();
        assertEquals(Either.right(0), either);
    }
}
