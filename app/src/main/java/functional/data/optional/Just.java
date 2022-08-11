package functional.data.optional;

import java.util.Objects;
import java.util.function.Function;

public final class Just<A> implements Optional<A> {

    private final A element;

    Just(A element) {
        this.element = element;
    }

    @Override
    public <C> C maybe(Function<A, C> f, C constant) {
        return f.apply(element);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Just<?> just = (Just<?>) o;
        return Objects.equals(element, just.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element);
    }

/*    @Override
    public <R> Optional<R> map(Function<A, R> f) {
        return new Just<>(f.apply(element));
    }

    @Override
    public <R> Optional<R> flatMap(Function<A, Monad<R, Optional<?>>> f) {
        return (Optional<R>) f.apply(element);
    }

    @Override
    public <R> Optional<R> fapply(Applicative<Function<A, R>, Optional<?>> applicative) {
        return ((Optional<Function<A, R>>) applicative).maybe(f -> Optional.of(f.apply(element)), Optional.of());
    }*/
}
