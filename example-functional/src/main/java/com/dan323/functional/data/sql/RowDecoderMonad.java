package com.dan323.functional.data.sql;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.data.either.Either;

import java.util.function.BiFunction;
import java.util.function.Function;

@Monad
@Alternative
public final class RowDecoderMonad implements IMonad<RowDecoder<?>>, IAlternative<RowDecoder<?>> {

    private RowDecoderMonad() {}

    private static final RowDecoderMonad INSTANCE = new RowDecoderMonad();

    public static RowDecoderMonad getInstance() {
        return INSTANCE;
    }

    public static <A> RowDecoder<A> pure(A value) {
        return rs -> Either.right(value);
    }

    public static <A, B> RowDecoder<B> map(RowDecoder<A> dec, Function<A, B> f) {
        return RowDecoder.map(dec, f);
    }

    public static <A, B> RowDecoder<B> fapply(RowDecoder<Function<A, B>> decF, RowDecoder<A> decA) {
        return flatMap(f -> map(decA, f), decF);
    }

    public static <A, B, C> RowDecoder<C> liftA2(BiFunction<A, B, C> f, RowDecoder<A> da, RowDecoder<B> db) {
        return RowDecoder.liftA2(f, da, db);
    }

    /** Chains two decoders: decodes A first, then uses the result to choose and run the next decoder. */
    public static <A, B> RowDecoder<B> flatMap(Function<A, RowDecoder<B>> f, RowDecoder<A> dec) {
        return rs -> dec.decode(rs).either(Either::left, a -> f.apply(a).decode(rs));
    }

    /** A decoder that always fails — the identity element for {@code disjunction}. */
    public static <A> RowDecoder<A> empty() {
        return RowDecoder.empty();
    }

    /** Tries {@code first}; falls back to {@code second} if the first decoder fails. */
    public static <A> RowDecoder<A> disjunction(RowDecoder<A> first, RowDecoder<A> second) {
        return rs -> first.decode(rs).either(err -> second.decode(rs), Either::right);
    }

    @Override
    public Class<RowDecoder<?>> getClassAtRuntime() {
        return (Class<RowDecoder<?>>) (Class<?>) RowDecoder.class;
    }
}
