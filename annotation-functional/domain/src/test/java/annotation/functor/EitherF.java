package annotation.functor;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public class EitherF<A,B> implements IFunctor<EitherF<A,?>> {

    public static <B,C,A> EitherF<A,C> map(EitherF<A,B> base, Function<B,C> map){
        return null;
    }

}
