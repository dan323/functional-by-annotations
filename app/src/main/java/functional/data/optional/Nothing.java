package functional.data.optional;

import java.util.Objects;
import java.util.function.Function;

public final class Nothing<A> implements Optional<A> {

    private Nothing(){
    }

    static final Nothing<?> NOTHING = new Nothing<>();

    @Override
    public <C> C maybe(Function<A, C> f, C constant) {
        return constant;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == NOTHING;
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(9);
    }

    /*
    @Override
    public <R> Optional<R> map(Function<A, R> f) {
        return Optional.of();
    }

    @Override
    public <R> Optional<R> flatMap(Function<A, Monad<R, Optional<?>>> f) {
        return Optional.of();
    }

    @Override
    public <Q> Optional<Q> fapply(Applicative<Function<A, Q>, Optional<?>> ff) {
        return Optional.of();
    }*/
}
