package functional.data.optional;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.function.Function;

@Functor
public sealed interface Optional<A> extends IFunctor<A, Optional<?>> permits Nothing, Just {

    <C> C maybe(Function<A, C> f, C constant);

    static <A> Optional<A> of(A element) {
        return new Just<>(element);
    }

    static <A> Optional<A> of() {
        return (Optional<A>) Nothing.NOTHING;
    }

    static <A,R> Optional<R> map(Optional<A> base, Function<A, R> f){
        return base.maybe(x -> Optional.of(f.apply(x)), Optional.of());
    }

    /*
    @Override
    <R> Optional<R> flatMap(Function<A, Monad<R, Optional<?>>> f);

    @Override
    <R> Optional<R> fapply(Applicative<Function<A, R>, Optional<?>> applicative);

    @Override
    default <Q> Optional<Q> pure(Q a) {
        return of(a);
    }

    @Override
    default <R> Optional<R> join(Monad<Monad<R, Optional<?>>, Optional<?>> monadMonad) {
        var optOpt = ((Optional<Optional<R>>) (Monad<?, ?>) monadMonad);
        return optOpt.maybe(Function.identity(), of());
    }*/
}
