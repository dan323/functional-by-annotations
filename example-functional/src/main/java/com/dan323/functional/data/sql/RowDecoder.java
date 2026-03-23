package com.dan323.functional.data.sql;

import com.dan323.functional.data.either.Either;

import java.sql.ResultSet;
import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface RowDecoder<A> {
    Either<DbError, A> decode(ResultSet rs);

    static <A,B> RowDecoder<B> map(RowDecoder<A> dec, Function<A, B> mapping){
        return rs -> dec.decode(rs).either(
                err -> Either.left(err),
                a -> Either.right(mapping.apply(a))
        );
    }

    static <A,B,C> RowDecoder<C> liftA2(BiFunction<A, B, C> mapping, RowDecoder<A> decA, RowDecoder<B> decB){
        return rs -> decA.decode(rs).either(
                err -> Either.left(err),
                a -> decB.decode(rs).either(
                        err -> Either.left(err),
                        b -> Either.right(mapping.apply(a,b))
                )
        );
    }

    static <A> RowDecoder<A> empty() {
       return rs -> Either.left(new DbError.DecodeError("Not implemented"));
    }
}
