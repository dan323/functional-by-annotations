package annotation.applicative;

import functional.annotation.Applicative;
import functional.annotation.iface.IApplicative;

import java.util.List;
import java.util.function.Function;

@Applicative
class NoPublicTypeApplicative implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a){
        return List.of(a);
    }

    public static <A,B> List<B> fapply(List<Function<A, B>> ffunction, List<A> base){
        return List.of();
    }
}
