package functional;

import functional.annotation.iface.util.ApplicativeUtil;
import functional.applicative.ApplicativeLiftA2Mock;
import functional.applicative.ApplicativeMock;
import functional.applicative.ApplicativeNoMapMock;
import functional.monad.EmptyMonad;
import functional.monad.MonadMock;
import functional.monad.MonadNoPure;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicativeUtilTest {

    @Test
    public void monadNoPure() {
        assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<MonadNoPure, List, List<Integer>, Integer>pure(MonadNoPure.class, List.class, 5));
    }

    @Test
    public void monadFapplyError(){
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<MonadNoPure, List, List<Integer>, List<Integer>, List<Function<Integer, Integer>>>fapply(MonadNoPure.class, List.class, List.of(1, 2), List.of(x -> x + 1, x -> x * 2)));
        assertTrue(ex.getMessage().contains("The monad is not correctly implemented"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<EmptyMonad, List, List<Integer>, List<Integer>, List<Function<Integer, Integer>>>fapply(EmptyMonad.class, List.class, List.of(1, 2), List.of(x -> x + 1, x -> x * 2)));
        assertTrue(ex2.getMessage().contains("The monad is not correctly implemented"));
    }
    @Test
    public void monandFapply() {
        var sol = ApplicativeUtil.<MonadMock, List, List<Integer>, List<Integer>, List<Function<Integer, Integer>>>fapply(MonadMock.class, List.class, List.of(2, 3, 4), List.of(x -> x + 1, x -> x - 3));
        assertEquals(List.of(3, 4, 5, -1, 0, 1), sol);
    }

    @Test
    public void pureTest() {
        var k = ApplicativeUtil.pure(ApplicativeMock.class, ApplicativeMock.class, 5);
        assertEquals(5, k.getA());
    }

    @Test
    public void fapplyTest() {
        ApplicativeMock<Function<Integer, Integer>> ff = new ApplicativeMock<>(x -> x + 1);
        ApplicativeMock<Integer> base = new ApplicativeMock<>(6);
        var k = ApplicativeUtil.fapply(ApplicativeMock.class, ApplicativeMock.class, base, ff);
        assertEquals(7, k.getA());

        k = ApplicativeUtil.fapply(ApplicativeLiftA2Mock.class, ApplicativeMock.class, base, ff);
        assertEquals(7, k.getA());
    }

    @Test
    public void applicativeLift2A() {
        ApplicativeMock<Integer> v = new ApplicativeMock<>(7);
        ApplicativeMock<Integer> w = new ApplicativeMock<>(9);
        var q = ApplicativeUtil.liftA2(ApplicativeMock.class, ApplicativeMock.class, Integer::sum, v, w);
        assertEquals(16, q.getA());

        q = ApplicativeUtil.liftA2(ApplicativeNoMapMock.class, ApplicativeMock.class, (Integer k, Integer s) -> k - s, v, w);
        assertEquals(-2, q.getA());
    }
}
