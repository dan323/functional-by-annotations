package annotation.applicative;

import functional.annotation.Applicative;
import functional.annotation.iface.IApplicative;

import java.util.List;

@Applicative
public class ApplicativeOnlyPure implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a){
        return List.of();
    }
}
