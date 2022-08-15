package functional;

import functional.annotation.Applicative;
import functional.annotation.iface.IApplicative;

import java.util.function.Function;

@Applicative
public final class ApplicativeMock<A> implements IApplicative<ApplicativeMock<?>> {

    private final A a;

    ApplicativeMock(A a) {
        this.a = a;
    }

    public A getA() {
        return a;
    }

    public static <A> ApplicativeMock<A> pure(A a) {
        return new ApplicativeMock<>(a);
    }

    public static <A, B> ApplicativeMock<B> fapply(ApplicativeMock<Function<A, B>> f, ApplicativeMock<A> base) {
        return new ApplicativeMock<>(f.a.apply(base.a));
    }

    public static <A, B> ApplicativeMock<B> map(ApplicativeMock<A> base, Function<A, B> function) {
        return new ApplicativeMock<>(function.apply(base.a));
    }

    @Override
    public String toString() {
        return a.toString();
    }
}
