package functional;

import functional.annotation.iface.util.MonadUtil;
import functional.monad.EmptyMonad;
import functional.monad.MonadMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MonadUtilTest {

    @Test
    public void monadFlatMap() {
        var sol = MonadUtil.flatMap(MonadMock.class, List.class, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4));
        assertEquals(List.of(2, 3, 3, 4, 4, 5), sol);
    }

    @Test
    public void monadFlatMapError() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MonadUtil.flatMap(EmptyMonad.class, List.class, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4)));
        assertTrue(exception.getMessage().contains("The monad is not correctly implemented"));
    }

    @Test
    public void monadJoin() {
        List<Integer> sol = MonadUtil.join(MonadMock.class, List.class, List.of(List.of(5,6),List.of(7,8)));
        assertEquals(List.of(5,6,7,8), sol);
    }

    @Test
    public void monadFailJoin() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> MonadUtil.join(EmptyMonad.class, List.class, List.of(List.of(5,6),List.of(7,8))));
        assertTrue(exception.getMessage().contains("The monad is not correctly implemented"));
    }

}
