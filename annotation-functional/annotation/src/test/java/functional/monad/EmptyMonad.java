package functional.monad;

import functional.annotation.Monad;
import functional.annotation.iface.IMonad;

import java.util.List;

@Monad
public final class EmptyMonad implements IMonad<List<?>> {
}
