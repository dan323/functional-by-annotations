package functional.data.list;

import functional.data.optional.Maybe;

import java.util.Objects;
import java.util.function.Function;

/**
 * Infinite list of one element repeating itself
 *
 * @param <A>
 */
final class Repeat<A> implements List<A> {

    private final A element;

    Repeat(A a) {
        if (a == null) throw new IllegalArgumentException("The repeated element must not be null.");
        this.element = a;
    }

    @Override
    public Maybe<A> head() {
        return Maybe.of(element);
    }

    @Override
    public List<A> tail() {
        return this;
    }

    @Override
    public <B> List<B> map(Function<A, B> mapping) {
        return new Repeat<>(mapping.apply(element));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Repeat<?>) {
            return Objects.equals(element, ((Repeat<?>) obj).element);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 7 * element.hashCode();
    }

    @Override
    public String toString() {
        return "[" + element + ", ...]";
    }
}
