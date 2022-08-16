package functional;

import functional.annotation.iface.util.MonadUtil;
import functional.monad.MonadMock;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonadUtilTest {

    @Test
    public void monadFlatMap() {
        var sol = MonadUtil.flatMap(MonadMock.class, List.class, (Integer x) -> List.of(x, x + 1), List.of(2, 3, 4));
        assertEquals(List.of(2, 3, 3, 4, 4, 5), sol);
    }

}
