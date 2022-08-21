package com.dan323.functional;

import com.dan323.functional.annotation.util.FunctorUtil;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeFunctor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaybeFunctorTest {

    @Test
    public void maybeFunctor() {
        Maybe<Boolean> mb = Maybe.of();
        Maybe<Boolean> mnotb = MaybeFunctor.map(mb, b -> !b);
        assertEquals(Maybe.of(), mnotb);

        mb = Maybe.of(true);
        mnotb = MaybeFunctor.map(mb, b -> !b);
        assertEquals(Maybe.of(false), mnotb);
    }

    @Test
    public void maybeFunctorUtils() {
        Maybe<Boolean> mb = Maybe.of();
        Maybe<Boolean> mnotb = FunctorUtil.<MaybeFunctor, Maybe, Maybe<Boolean>, Maybe<Boolean>, Boolean, Boolean>map(MaybeFunctor.MAYBE, Maybe.class, mb, b -> !b);
        assertEquals(Maybe.of(), mnotb);

        mb = Maybe.of(true);
        mnotb = FunctorUtil.<MaybeFunctor, Maybe, Maybe<Boolean>, Maybe<Boolean>, Boolean, Boolean>map(MaybeFunctor.MAYBE, Maybe.class, mb, b -> !b);
        assertEquals(Maybe.of(false), mnotb);
    }
}
