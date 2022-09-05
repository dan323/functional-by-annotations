package com.dan323.functional.util.monad;

import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.compiler.util.MonadUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class MonadUtilTest {

    @Test
    public void monadFapplyError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.fapply(MonadNoPure.MONAD, List.of(1, 2), List.<Function<Integer, Integer>>of(x -> x + 1, x -> x * 2)));
        assertTrue(ex.getMessage().contains("The functor is not correctly"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.fapply(EmptyMonad.MONAD, List.of(1, 2), List.<Function<Integer, Integer>>of(x -> x + 1, x -> x * 2)));
        assertTrue(ex2.getMessage().contains("The applicative is not correctly"));
    }

    @Test
    public void monandFapply() {
        var sol = ApplicativeUtil.fapply(MonadMock.MONAD, List.of(2, 3, 4), List.<Function<Integer,Integer>>of(x -> x + 1, x -> x - 3));
        assertEquals(List.of(3, 4, 5, -1, 0, 1), sol);
    }

    @Test
    public void monadLiftA2() {
        List<Integer> v = List.of(5, 6);
        List<Integer> w = List.of(-1, -2);
        List<?> q = ApplicativeUtil.liftA2(MonadMock.MONAD, Integer::sum, v, w);
        assertEquals(List.of(4, 3, 5, 4), q);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.liftA2(EmptyMonad.MONAD, Integer::sum, v, w));
        assertTrue(exception.getMessage().contains("The applicative is not correctly implemented"));
    }

    @Test
    public void monadNoPure() {
        assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.pure(MonadNoPure.MONAD, 5));
    }

    @Test
    public void monadFlatMap() {
        var sol = MonadUtil.flatMap(MonadMock.MONAD, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4));
        assertEquals(List.of(2, 3, 3, 4, 4, 5), sol);
    }

    @Test
    public void monadFlatMapError() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MonadUtil.flatMap(EmptyMonad.MONAD, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4)));
        assertTrue(exception.getMessage().contains("The monad is not correctly implemented"));
    }

    @Test
    public void monadJoin() {
        List<?> sol = MonadUtil.join(MonadMock.MONAD, List.of(List.of(5, 6), List.of(7, 8)));
        assertEquals(List.of(5, 6, 7, 8), sol);
    }

    @Test
    public void monadFailJoin() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MonadUtil.join(EmptyMonad.MONAD, List.of(List.of(5, 6), List.of(7, 8))));
        assertTrue(exception.getMessage().contains("The monad is not correctly implemented"));
    }


    @Test
    public void monandMap() {
        var sol = FunctorUtil.map(MonadMock.MONAD, List.of(2, 3, 4), (Function<Integer,Integer>) x -> x + 2);
        assertEquals(List.of(4, 5, 6), sol);
    }

    @Test
    public void flatMapFromJoin() {
        var sol = MonadUtil.join(new MonadJoin(), Optional.empty());
        assertEquals(Optional.empty(), sol);
        sol = MonadUtil.join(new MonadJoin(), Optional.of(Optional.empty()));
        assertEquals(Optional.empty(), sol);
        sol = MonadUtil.join(new MonadJoin(), Optional.of(Optional.of(5)));
        assertEquals(Optional.of(5), sol);
    }

}
