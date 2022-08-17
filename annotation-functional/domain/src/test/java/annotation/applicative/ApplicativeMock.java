package annotation.applicative;

import functional.annotation.Applicative;
import functional.annotation.iface.IApplicative;

import java.util.List;
import java.util.function.Function;

@Applicative
public class ApplicativeMock implements IApplicative<List<?>> {


    public static <A> List<A> pure(A a) {
        return List.of(a);
    }

    public static <A, B> List<B> fapply(List<Function<A, B>> ffunction, List<A> base) {
        return ffunction.stream().flatMap(f -> base.stream().map(f)).toList();
    }

    public static <A, B> List<B> map(List<A> base, Function<A, B> mapping) {
        return base.stream().map(mapping).toList();
    }
}
