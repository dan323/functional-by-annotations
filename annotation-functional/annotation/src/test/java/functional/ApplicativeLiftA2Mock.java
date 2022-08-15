package functional;

import functional.annotation.Applicative;
import functional.annotation.iface.IApplicative;

import java.util.function.BiFunction;

@Applicative
public final class ApplicativeLiftA2Mock implements IApplicative<ApplicativeMock<?>> {

    public static <A> ApplicativeMock<A> pure(A a) {
        return new ApplicativeMock<>(a);
    }

    public static <A, B, C> ApplicativeMock<C> liftA2(BiFunction<A, B, C> f, ApplicativeMock<A> fa, ApplicativeMock<B> fb) {
        return pure(f.apply(fa.getA(), fb.getA()));
    }
}