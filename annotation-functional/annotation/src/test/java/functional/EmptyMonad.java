package functional;

import functional.annotation.Monad;
import functional.annotation.iface.IMonad;

import java.util.List;

@Monad
public class EmptyMonad implements IMonad<List<?>> {
}
