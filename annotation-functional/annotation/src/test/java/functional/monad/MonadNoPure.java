package functional.monad;

import functional.annotation.Monad;
import functional.annotation.iface.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public final class MonadNoPure implements IMonad<List<?>> {

    public static <A,B> List<B> flatMap(Function<A,List<B>> map, List<A> lst){
        return lst.stream().flatMap(x -> map.apply(x).stream()).toList();
    }
}
