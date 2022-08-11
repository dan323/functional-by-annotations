package functional;

import functional.annotation.iface.FunctorUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IFunctorUtilTest {

    @Test
    public void mapTest() {
        var lst = List.of(1, 2, 3);
        var sol = FunctorUtils.<FunctorMock, FunctorMock<?>, FunctorMock<Integer>, FunctorMock<Integer>, Integer, Integer>map(FunctorMock.class, new FunctorMock<>(lst), x -> x + 1);
        assertEquals(List.of(2, 3, 4), sol.toList());
    }
}
