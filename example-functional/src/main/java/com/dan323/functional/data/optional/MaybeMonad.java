package com.dan323.functional.data.optional;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.data.util.alternative.AlternativeMonoid;

import java.util.function.Function;

@Monad
@Alternative
public class MaybeMonad implements IMonad<Maybe<?>>, IAlternative<Maybe<?>> {

    private MaybeMonad() {
    }

    public static <A> Maybe<A> join(Maybe<Maybe<A>> maybeMaybe) {
        return maybeMaybe.maybe(Function.identity(), Maybe.of());
    }

    public static <A> Maybe<A> disjunction(Maybe<A> fst, Maybe<A> snd){
        return fst.maybe(Maybe::of, snd);
    }

    public static <A> AlternativeMonoid<Maybe<A>,Maybe<?>> getAlternativeMonoid(){
        return new AlternativeMonoid<>(getInstance());
    }

    public static <A> Maybe<A> empty(){
        return Maybe.of();
    }

    public static <A, B> Maybe<B> fapply(Maybe<Function<A, B>> maybeFun, Maybe<A> base) {
        return maybeFun.maybe(f -> map(base, f), Maybe.of());
    }

    public static MaybeMonad getInstance() {
        return MAYBE;
    }

    private static final MaybeMonad MAYBE = new MaybeMonad();

    public static <A, R> Maybe<R> map(Maybe<A> base, Function<A, R> f) {
        return base.maybe(x -> Maybe.of(f.apply(x)), Maybe.of());
    }

    public static <A> Maybe<A> pure(A a) {
        return Maybe.of(a);
    }

    @Override
    public Class<Maybe<?>> getClassAtRuntime() {
        return (Class<Maybe<?>>) (Class) Maybe.class;
    }
}
