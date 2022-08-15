package functional;

import functional.annotation.Monad;
import functional.annotation.iface.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public class MonadMock implements IMonad<List<?>> {

    public static <A,B> List<B> flatMap(Function<A,List<B>> f, List<A> base){
        return base.stream().flatMap(x -> f.apply(x).stream()).toList();
    }

    public static <A> List<A> pure(A a){
        return List.of(a);
    }

}
