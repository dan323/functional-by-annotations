package annotation.monad;

import functional.annotation.Monad;
import functional.annotation.iface.IMonad;

import java.util.List;
import java.util.function.Function;

@Monad
public class MonadJoinApplicativeFapply implements IMonad<List<?>> {

    public static <A> List<A> pure(A a) {
        return List.of();
    }

    public static <A, B> List<B> fapply(List<Function<A, B>> ffunction, List<A> base) {
        return ffunction.stream().flatMap(f -> base.stream().map(f)).toList();
    }

    public static <B> List<B> join(List<List<B>> base) {
        return List.of();
    }
}
