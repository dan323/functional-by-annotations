package functional.data.either;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public class FLeftEither<A, B> implements IFunctor<A, FLeftEither<?, B>> {

    private final Either<A, B> either;

    public FLeftEither(Either<A, B> either) {
        this.either = either;
    }

    public static <A,C,B> FLeftEither<C,B> map(FLeftEither<A,B> base, Function<A,C> mapping){
        return base.either.either(b -> new FLeftEither<>(Either.left(mapping.apply(b))), x -> new FLeftEither<>(Either.right(x)));
    }

    /*
    @Override
    public <R> FLeftEither<R, B> pure(R r) {
        return new FLeftEither<>(Either.left(r));
    }

    @Override
    public <R> FLeftEither<R, B> join(Monad<Monad<R, FLeftEither<?, B>>, FLeftEither<?, B>> monadMonad) {
        var fleft = ((FLeftEither<FLeftEither<R, B>, B>) (Monad<?, ?>) monadMonad);
        return fleft.toEither().either(Function.identity(), b -> new FLeftEither<>(Either.right(b)));
    }

    @Override
    public <R> FLeftEither<R, B> map(Function<A, R> f) {
        Function<A, Either<R, B>> g = a -> Either.left(f.apply(a));
        return new FLeftEither<>(either.either(g, Either::right));
    }

    public Either<A, B> toEither() {
        return either;
    }

    @Override
    public <R> FLeftEither<R, B> flatMap(Function<A, Monad<R, FLeftEither<?, B>>> f) {
        var g = (Function<A,FLeftEither<R,B>>)(Function<?,?>)f;
        return either.either(g,b -> new FLeftEither<>(Either.right(b)));
    }*/
}
