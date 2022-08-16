package annotation.functor;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public class EitherF<A> implements IFunctor<EitherF.Either<A,?>> {

    public static <B,C,A> Either<A,C> map(Either<A,B> base, Function<B,C> map){
        return new Either<>() {
            @Override
            public A getLeft() {
                return base.getLeft();
            }

            @Override
            public C getRight() {
                return map.apply(base.getRight());
            }
        };
    }

    interface Either<A,B> {
        A getLeft();
        B getRight();
    }
}
