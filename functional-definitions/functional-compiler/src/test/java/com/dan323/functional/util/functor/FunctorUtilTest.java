package com.dan323.functional.util.functor;

import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctorUtilTest {

    @Test
    public void mapTest() {
        var lst = List.of(1, 2, 3);
        var sol = FunctorUtil.map(FunctorMock.FUNCTOR, lst, (Integer x) -> x + 1);
        assertEquals(List.of(2, 3, 4), sol);
    }

    @Test
    public void mapConstTest() {
        var lst = List.of(1, 2, 3);
        var sol = FunctorUtil.mapConst(FunctorMock.FUNCTOR, lst, 7);
        assertEquals(List.of(7, 7, 7), sol);
        var sol2 = FunctorUtil.mapConst(new FunctorMapConst(), Optional.of(4), 5);
        assertEquals(Optional.empty(), sol2);
    }

}
