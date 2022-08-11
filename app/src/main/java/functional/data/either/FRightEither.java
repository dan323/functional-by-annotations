package functional.data.either;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public class FRightEither<A, B> implements IFunctor<B, FRightEither<A, ?>> {

    private final Either<A, B> either;

    public FRightEither(Either<A, B> either) {
        this.either = either;
    }

    public Either<A,B> toEither(){
        return either;
    }

    public static <B,C,A> FRightEither<A,C> map(FRightEither<A,B> base, Function<B,C> mapping){
        return base.either.either(x -> new FRightEither<>(Either.left(x)), b -> new FRightEither<>(Either.right(mapping.apply(b))));
    }
    /*
    @Override
    public <R> FRightEither<A, R> pure(R r) {
        return new FRightEither<>(Either.right(r));
    }

    @Override
    public <R> FRightEither<A, R> join(Monad<Monad<R, FRightEither<A, ?>>, FRightEither<A, ?>> monadMonad) {
        var g = (FRightEither<A,FRightEither<A,R>>)(Monad<?,?>)monadMonad;
        return g.either.either(a -> new FRightEither<>(Either.left(a)), Function.identity());
    }

    @Override
    public <R> FRightEither<A, R> map(Function<B, R> f) {
        Function<B, Either<A, R>> g = a -> Either.right(f.apply(a));
        return new FRightEither<>(either.either(Either::left, g));
    }

    @Override
    public <R> FRightEither<A, R> flatMap(Function<B, Monad<R, FRightEither<A, ?>>> f) {
        var g = (Function<B,FRightEither<A,R>>)(Function<?,?>)f;


    }

    @Override
    public <Q> FRightEither<A,Q> fapply(Applicative<Function<B, Q>, FRightEither<A, ?>> ff) {
        return Monad.super.fapply(ff);
    }
    */
}
