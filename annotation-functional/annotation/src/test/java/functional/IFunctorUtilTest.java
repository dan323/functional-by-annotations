package functional;

import functional.annotation.iface.util.ApplicativeUtil;
import functional.annotation.iface.util.FunctorUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class IFunctorUtilTest {

    @Test
    public void mapTest() {
        var lst = List.of(1, 2, 3);
        var sol = FunctorUtil.map(FunctorMock.class, FunctorMock.class, new FunctorMock<>(lst), (Integer x) -> x + 1);
        assertEquals(List.of(2, 3, 4), sol.toList());
    }

    @Test
    public void monadNoPure(){
        assertThrows(IllegalArgumentException.class, () -> ApplicativeUtil.<MonadNoPure,List,List<Integer>,Integer>pure(MonadNoPure.class, List.class, 5));
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
