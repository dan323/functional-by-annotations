package com.dan323.functional.functor;

import com.dan323.functional.annotation.util.FunctorUtil;
import com.dan323.functional.monad.MonadMock;
import com.dan323.functional.applicative.ApplicativeMock;
import com.dan323.functional.applicative.ApplicativeNoMapMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctorUtilTest {

    @Test
    public void mapTest() {
        var lst = List.of(1, 2, 3);
        var sol = FunctorUtil.map(FunctorMock.class, FunctorMock.class, new FunctorMock<>(lst), (Integer x) -> x + 1);
        assertEquals(List.of(2, 3, 4), sol.toList());
    }

    @Test
    public void monandMap() {
        var sol = FunctorUtil.<MonadMock, List, List<Integer>, List<Integer>, Integer, Integer>map(MonadMock.class, List.class, List.of(2, 3, 4), x -> x + 2);
        assertEquals(List.of(4, 5, 6), sol);
    }

    @Test
    public void applicativeMap() {
        ApplicativeMock<Integer> v = new ApplicativeMock<>(7);
        var q = FunctorUtil.map(ApplicativeMock.class, ApplicativeMock.class, v, (Integer k) -> k + 1);
        assertEquals(8, q.getA());

        q = FunctorUtil.map(ApplicativeNoMapMock.class, ApplicativeMock.class, v, (Integer k) -> k + 1);
        assertEquals(8, q.getA());
    }
}
