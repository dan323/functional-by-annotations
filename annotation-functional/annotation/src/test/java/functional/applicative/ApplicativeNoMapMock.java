package functional.applicative;

import functional.annotation.Applicative;
import functional.annotation.iface.IApplicative;

import java.util.function.Function;

@Applicative
public final class ApplicativeNoMapMock implements IApplicative<ApplicativeMock<?>> {

    public static <A> ApplicativeMock<A> pure(A a) {
        return new ApplicativeMock<>(a);
    }

    public static <A, B> ApplicativeMock<B> fapply(ApplicativeMock<Function<A, B>> f, ApplicativeMock<A> base) {
        return new ApplicativeMock<>(f.getA().apply(base.getA()));
    }
}