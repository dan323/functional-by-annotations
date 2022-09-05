package com.dan323.functional;

import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaybeFunctorTest {

    @Test
    public void maybeFunctor() {
        Maybe<Boolean> mb = Maybe.of();
        Maybe<Boolean> mnotb = MaybeMonad.map(mb, b -> !b);
        assertEquals(Maybe.of(), mnotb);

        mb = Maybe.of(true);
        mnotb = MaybeMonad.map(mb, b -> !b);
        assertEquals(Maybe.of(false), mnotb);
    }

    @Test
    public void maybeFunctorUtils() {
        Maybe<Boolean> mb = Maybe.of();
        Maybe<Boolean> mnotb = (Maybe<Boolean>) FunctorUtil.map(MaybeMonad.getInstance(), mb, (Boolean b) -> !b);
        assertEquals(Maybe.of(), mnotb);

        mb = Maybe.of(true);
        mnotb = (Maybe<Boolean>) FunctorUtil.map(MaybeMonad.getInstance(), mb, (Boolean b) -> !b);
        assertEquals(Maybe.of(false), mnotb);
    }
}
