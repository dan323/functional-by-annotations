package functional;

import functional.annotation.iface.util.FunctorUtil;
import functional.applicative.ApplicativeMock;
import functional.applicative.ApplicativeNoMapMock;
import functional.functor.FunctorMock;
import functional.monad.MonadMock;
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
