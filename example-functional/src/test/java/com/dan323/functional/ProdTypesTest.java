package com.dan323.functional;

import com.dan323.functional.annotation.util.FunctorUtil;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.either.LeftEither;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeApplicative;
import com.dan323.functional.data.pair.PairTypeContructor;
import com.dan323.functional.data.pair.ProdApplicative;
import com.dan323.functional.data.pair.ProdFunctor;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProdTypesTest {

    @Test
    public void prodType() {
        var pair = new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(1, 2, 3), Maybe.of(5));
        assertEquals(FiniteList.of(1, 2, 3), pair.getFirst());
        assertEquals(Maybe.of(5), pair.getSecond());
    }

    @Test
    public void prodFunctorWithApplicative() {
        var pairFunctor = new ProdFunctor<>(FiniteListFunctional.getInstance(), MaybeApplicative.MAYBE);
        var pair = new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(1, 2, 3), Maybe.of(5));
        var sol = pairFunctor.map(pair, (Integer x) -> x * 2);
        assertEquals(new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(2, 4, 6), Maybe.of(10)), sol);
    }

    @Test
    public void prodFunctorWithMonad() {
        var pairFunctor = new ProdFunctor<>(FiniteListFunctional.getInstance(), LeftEither.<Integer>getInstance());
        var pair = new PairTypeContructor<FiniteList<?>, Either<?, Integer>, Integer>(FiniteList.of(1, 2, 3), Either.left(8));
        var sol = pairFunctor.map(pair, (Integer x) -> x * 2);
        assertEquals(new PairTypeContructor<FiniteList<?>, Either<?, Integer>, Integer>(FiniteList.of(2, 4, 6), Either.left(16)), sol);
    }

    @Test
    public void prodApplicative() {
        var pairApplicative = new ProdApplicative<>(FiniteListFunctional.getInstance(), MaybeApplicative.MAYBE);
        var pairFun = new PairTypeContructor<FiniteList<?>, Maybe<?>, Function<Integer, Integer>>(FiniteList.<Function<Integer, Integer>>of(x -> x + 1), Maybe.<Function<Integer, Integer>>of(x -> x * 2));
        var pair = new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(1, 2), Maybe.of());
        var sol = pairApplicative.fapply(pairFun, pair);
        assertEquals(new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(2, 3), Maybe.of()), sol);
    }

    @Test
    public void prodApplicativeAsFunctor() {
        var pairFunctor = new ProdApplicative<>(FiniteListFunctional.getInstance(), MaybeApplicative.MAYBE);
        var pair = new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(1, 2, 3), Maybe.of(5));
        var sol = pairFunctor.map(pair, (Integer x) -> x * 2);
        assertEquals(new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(2, 4, 6), Maybe.of(10)), sol);
    }

    @Test
    public void prodApplicativeAsFunctorRefl() {
        var pairFunctor = new ProdApplicative<>(FiniteListFunctional.getInstance(), MaybeApplicative.MAYBE);
        var pair = new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(1, 2, 3), Maybe.of(5));
        var sol = FunctorUtil.map(pairFunctor, PairTypeContructor.class, pair, (Integer x) -> x * 2);
        assertEquals(new PairTypeContructor<FiniteList<?>, Maybe<?>, Integer>(FiniteList.of(2, 4, 6), Maybe.of(10)), sol);
    }
}
