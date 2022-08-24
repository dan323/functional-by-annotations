package com.dan323.functional.data.optional;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.Function;

@Applicative
public class MaybeApplicative implements IApplicative<Maybe<?>> {

    private MaybeApplicative() {

    }

    public static <A> Maybe<A> join(Maybe<Maybe<A>> maybeMaybe) {
        return maybeMaybe.maybe(Function.identity(), Maybe.of());
    }

    public static <A,B> Maybe<B> fapply(Maybe<Function<A,B>> maybeFun, Maybe<A> base){
        return maybeFun.maybe(f -> map(base, f), Maybe.of());
    }

    public static final MaybeApplicative MAYBE = new MaybeApplicative();

    public static <A, R> Maybe<R> map(Maybe<A> base, Function<A, R> f) {
        return base.maybe(x -> Maybe.of(f.apply(x)), Maybe.of());
    }

    public static <A> Maybe<A> pure(A a) {
        return Maybe.of(a);
    }
}
