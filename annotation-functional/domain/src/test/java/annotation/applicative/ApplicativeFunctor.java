package annotation.applicative;

import functional.annotation.Functor;
import functional.annotation.iface.IApplicative;

import java.util.List;
import java.util.function.BiFunction;

@Functor
public class ApplicativeFunctor implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a){
        return List.of();
    }

    public static <A, B, C> List<C> liftA2(BiFunction<A, B, C> function, List<A> base1, List<B> base2) {
        return List.of();
    }
}
