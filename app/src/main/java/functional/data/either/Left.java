package functional.data.either;

import java.util.Objects;
import java.util.function.Function;

public final class Left<A, B> implements Either<A, B> {

    private final A a;

    Left(A a) {
        this.a = a;
    }

    public <C> C either(Function<A, C> aToC, Function<B, C> bToC) {
        if (aToC == null) {
            throw new IllegalArgumentException();
        } else {
            return aToC.apply(a);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        return Objects.equals(a, ((Left<A, B>) obj).a);
    }

    @Override
    public int hashCode() {
        return 7 * Objects.hashCode(a);
    }
}
