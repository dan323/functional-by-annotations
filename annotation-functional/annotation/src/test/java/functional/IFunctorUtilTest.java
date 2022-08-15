package functional;

import functional.annotation.iface.util.ApplicativeUtil;
import functional.annotation.iface.util.FunctorUtil;
import functional.annotation.iface.util.MonadUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class IFunctorUtilTest {

    @Test
    public void mapTest() {
        var lst = List.of(1, 2, 3);
        var sol = FunctorUtil.map(FunctorMock.class, FunctorMock.class, new FunctorMock<>(lst), (Integer x) -> x + 1);
        assertEquals(List.of(2, 3, 4), sol.toList());
    }

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
    public void monadFlatMap() {
        var sol = MonadUtil.flatMap(MonadMock.class, List.class, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4));
        assertEquals(List.of(2, 3, 3, 4, 4, 5), sol);
    }

    @Test
    public void monandMap() {
        var sol = FunctorUtil.<MonadMock, List, List<Integer>, List<Integer>, Integer, Integer>map(MonadMock.class, List.class, List.of(2, 3, 4), x -> x + 2);
        assertEquals(List.of(4, 5, 6), sol);
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
    public void applicativeMap() {
        ApplicativeMock<Integer> v = new ApplicativeMock<>(7);
        var q = FunctorUtil.map(ApplicativeMock.class, ApplicativeMock.class, v, (Integer k) -> k + 1);
        assertEquals(8, q.getA());

        q = FunctorUtil.map(ApplicativeNoMapMock.class, ApplicativeMock.class, v, (Integer k) -> k + 1);
        assertEquals(8, q.getA());
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
