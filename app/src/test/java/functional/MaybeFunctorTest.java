package functional;

import functional.annotation.iface.util.FunctorUtil;
import functional.data.optional.Maybe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaybeFunctorTest {

    @Test
    public void maybeFunctor() {
        Maybe<Boolean> mb = Maybe.of();
        Maybe<Boolean> mnotb = Maybe.map(mb, b -> !b);
        assertEquals(Maybe.of(), mnotb);

        mb = Maybe.of(true);
        mnotb = Maybe.map(mb, b -> !b);
        assertEquals(Maybe.of(false), mnotb);
    }

    @Test
    public void maybeFunctorUtils() {
        Maybe<Boolean> mb = Maybe.of();
        Maybe<Boolean> mnotb = FunctorUtil.<Maybe, Maybe, Maybe<Boolean>, Maybe<Boolean>, Boolean, Boolean>map(Maybe.class, Maybe.class, mb, b -> !b);
        assertEquals(Maybe.of(), mnotb);

        mb = Maybe.of(true);
        mnotb = FunctorUtil.<Maybe, Maybe, Maybe<Boolean>, Maybe<Boolean>, Boolean, Boolean>map(Maybe.class, Maybe.class, mb, b -> !b);
        assertEquals(Maybe.of(false), mnotb);
    }
}
