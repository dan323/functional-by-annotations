package com.dan323.functional.monad;

import com.dan323.functional.annotation.util.ApplicativeUtil;
import com.dan323.functional.annotation.util.FunctorUtil;
import com.dan323.functional.annotation.util.MonadUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class MonadUtilTest {

    @Test
    public void monadFapplyError() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<MonadNoPure, List<?>, List, List<Integer>, List<Integer>, List<Function<Integer, Integer>>>fapply(MonadNoPure.MONAD, List.class, List.of(1, 2), List.of((Integer x) -> x + 1, (Integer x) -> x * 2)));
        assertTrue(ex.getMessage().contains("The functor is not correctly implemented"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<EmptyMonad, List<?>, List, List<Integer>, List<Integer>, List<Function<Integer, Integer>>>fapply(EmptyMonad.MONAD, List.class, List.of(1, 2), List.of(x -> x + 1, x -> x * 2)));
        assertTrue(ex2.getMessage().contains("The applicative is not correctly implemented"));
    }

    @Test
    public void monandFapply() {
        var sol = ApplicativeUtil.<MonadMock, List<?>, List, List<Integer>, List<Integer>, List<Function<Integer, Integer>>>fapply(MonadMock.MONAD, List.class, List.of(2, 3, 4), List.of(x -> x + 1, x -> x - 3));
        assertEquals(List.of(3, 4, 5, -1, 0, 1), sol);
    }

    @Test
    public void monadLiftA2() {
        List<Integer> v = List.of(5, 6);
        List<Integer> w = List.of(-1, -2);
        List<Integer> q = ApplicativeUtil.liftA2(MonadMock.MONAD, List.class, Integer::sum, v, w);
        assertEquals(List.of(4, 3, 5, 4), q);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<EmptyMonad, List<?>, List, List<Integer>, List<Integer>, List<Integer>, Integer, Integer, Integer>liftA2(EmptyMonad.MONAD, List.class, Integer::sum, v, w));
        assertTrue(exception.getMessage().contains("The applicative is not correctly implemented"));
    }

    @Test
    public void monadNoPure() {
        assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<MonadNoPure, List<?>, List, List<Integer>, Integer>pure(MonadNoPure.MONAD, List.class, 5));
    }

    @Test
    public void monadFlatMap() {
        var sol = MonadUtil.flatMap(MonadMock.MONAD, List.class, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4));
        assertEquals(List.of(2, 3, 3, 4, 4, 5), sol);
    }

    @Test
    public void monadFlatMapError() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MonadUtil.flatMap(EmptyMonad.MONAD, List.class, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4)));
        assertTrue(exception.getMessage().contains("The monad is not correctly implemented"));
    }

    @Test
    public void monadJoin() {
        List<Integer> sol = MonadUtil.join(MonadMock.MONAD, List.class, List.of(List.of(5, 6), List.of(7, 8)));
        assertEquals(List.of(5, 6, 7, 8), sol);
    }

    @Test
    public void monadFailJoin() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MonadUtil.join(EmptyMonad.MONAD, List.class, List.of(List.of(5, 6), List.of(7, 8))));
        assertTrue(exception.getMessage().contains("The monad is not correctly implemented"));
    }


    @Test
    public void monandMap() {
        var sol = FunctorUtil.<MonadMock, List<?>, List, List<Integer>, List<Integer>, Integer, Integer>map(MonadMock.MONAD, List.class, List.of(2, 3, 4), x -> x + 2);
        assertEquals(List.of(4, 5, 6), sol);
    }

    @Test
    public void flatMapFromJoin() {
        var sol = MonadUtil.join(new MonadJoin(), Optional.class, Optional.empty());
        assertEquals(Optional.empty(), sol);
        sol = MonadUtil.join(new MonadJoin(), Optional.class, Optional.of(Optional.empty()));
        assertEquals(Optional.empty(), sol);
        sol = MonadUtil.join(new MonadJoin(), Optional.class, Optional.of(Optional.of(5)));
        assertEquals(Optional.of(5), sol);
    }

}
