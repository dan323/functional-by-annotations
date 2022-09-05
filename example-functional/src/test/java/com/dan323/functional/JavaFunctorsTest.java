package com.dan323.functional;

import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.data.list.JListFunctor;
import com.dan323.functional.data.optional.JOptionalFunctor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JavaFunctorsTest {

    @Test
    public void optionalFunctor() {
        var opt = JOptionalFunctor.map(Optional.of(8), k -> k + 1);
        assertEquals(Optional.of(9), opt);

        opt = JOptionalFunctor.<Integer, Integer>map(Optional.empty(), k -> k + 1);
        assertEquals(Optional.empty(), opt);

        opt = (Optional<Integer>) FunctorUtil.map(JOptionalFunctor.FUNCTOR, Optional.empty(), (Integer k) -> k + 1);
        assertEquals(Optional.empty(), opt);
    }

    @Test
    public void javaListFunctor() {
        var lst = JListFunctor.map(List.of(3, 3, 4), k -> k * k);
        assertEquals(List.of(9, 9, 16), lst);
        lst = (List<Integer>) FunctorUtil.map(JListFunctor.FUNCTOR, List.of(3, 3, 4), (Integer k) -> k * k);
        assertEquals(List.of(9, 9, 16), lst);
    }
}
