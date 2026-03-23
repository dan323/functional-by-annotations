package com.dan323.functional.data.sql;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.funcs.IAlternative;

import com.dan323.functional.data.either.Either;

import java.util.function.BiFunction;
import java.util.function.Function;

@Alternative
public class QueryFunctor implements IAlternative<Query<?>> {

    @Override
    public Class<Query<?>> getClassAtRuntime() {
        return (Class<Query<?>>) (Class) Query.class;
    }

    public static <A, B> Query<B> map(Query<A> base, Function<A, B> mapping) {
        return new Query<>(base.sql(), RowDecoder.map(base.decoder(), mapping));
    }

    public static <A> Query<A> pure(A value) {
        return new Query<>(new SqlAst.Pure(), rs -> Either.right(value));
    }

    public static <A> Query<A> empty() {
        return new Query<>(new SqlAst.Empty(), RowDecoder.empty());
    }

    public static <A> Query<A> disjunction(Query<A> first, Query<A> snd) {
        return new Query<>(new SqlAst.Union(first.sql(), snd.sql()), first.decoder());
    }

    public static <A,B,C> Query<C> liftA2(BiFunction<A, B, C> mapping, Query<A> qa, Query<B> qb) {
        return new Query<>(new SqlAst.Join(qa.sql(), qb.sql(), null), RowDecoder.liftA2(mapping, qa.decoder(), qb.decoder()));
    }

}
