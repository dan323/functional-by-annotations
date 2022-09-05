package com.dan323.functional.util.applicative;

import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.util.Identity;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicativeUtilTest {

    @Test
    public void pureTest() {
        var k = ApplicativeUtil.pure(ApplicativeMock.APPLICATIVE, 5);
        assertEquals(5, k.get());
    }

    @Test
    public void keepLeftTest() {
        var sol = ApplicativeUtil.keepLeft(ApplicativeMock.APPLICATIVE, new Identity<>(2), new Identity<>(5));
        assertEquals(2, sol.get());
        sol = ApplicativeUtil.keepLeft(ApplicativeMock.APPLICATIVE, new Identity<>(3), new Identity<>(5));
        assertEquals(3, sol.get());
    }

    @Test
    public void keepRightTest() {
        var sol = ApplicativeUtil.keepRight(ApplicativeMock.APPLICATIVE, new Identity<>(2), new Identity<>(5));
        assertEquals(5, sol.get());
        sol = ApplicativeUtil.keepRight(ApplicativeMock.APPLICATIVE, new Identity<>(3), new Identity<>(5));
        assertEquals(5, sol.get());
    }

    @Test
    public void fapplyTest() {
        Identity<Function<Integer, Integer>> ff = new Identity<>(x -> x + 1);
        Identity<Integer> base = new Identity<>(6);
        var k = ApplicativeUtil.fapply(ApplicativeMock.APPLICATIVE, base, ff);
        assertEquals(7, k.get());

        k = ApplicativeUtil.fapply(ApplicativeLiftA2Mock.APPLICATIVE, base, ff);
        assertEquals(7, k.get());
    }

    @Test
    public void applicativeLiftA2() {
        Identity<Integer> v = new Identity<>(7);
        Identity<Integer> w = new Identity<>(9);
        var q = ApplicativeUtil.liftA2(ApplicativeMock.APPLICATIVE, Integer::sum, v, w);
        assertEquals(16, q.get());

        q = ApplicativeUtil.liftA2(ApplicativeNoMapMock.APPLICATIVE, (Integer k, Integer s) -> k - s, v, w);
        assertEquals(-2, q.get());
    }

    @Test
    public void applicativeMap() {
        Identity<Integer> v = new Identity<>(7);
        var q = FunctorUtil.map(ApplicativeMock.APPLICATIVE, v, (Integer k) -> k + 1);
        assertEquals(8, q.get());

        q = FunctorUtil.map(ApplicativeNoMapMock.APPLICATIVE, v, (Integer k) -> k + 1);
        assertEquals(8, q.get());
    }
}
