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
        var sol = FunctorUtil.map(FunctorMock.FUNCTOR, List.class, lst, (Integer x) -> x + 1);
        assertEquals(List.of(2, 3, 4), sol);
    }

}
