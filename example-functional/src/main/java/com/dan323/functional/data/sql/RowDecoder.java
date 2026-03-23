package com.dan323.functional.data.sql;

import com.dan323.functional.data.either.Either;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    static RowDecoder<String> string(String column) {
        return rs -> {
            try {
                String value = rs.getString(column);
                return value != null
                        ? Either.right(value)
                        : Either.left(new DbError.DecodeError("null value in column: " + column));
            } catch (SQLException e) {
                return Either.left(new DbError.DecodeError(e.getMessage()));
            }
        };
    }

    static RowDecoder<Integer> integer(String column) {
        return rs -> {
            try {
                int value = rs.getInt(column);
                return rs.wasNull()
                        ? Either.left(new DbError.DecodeError("null value in column: " + column))
                        : Either.right(value);
            } catch (SQLException e) {
                return Either.left(new DbError.DecodeError(e.getMessage()));
            }
        };
    }

    static RowDecoder<Long> longCol(String column) {
        return rs -> {
            try {
                long value = rs.getLong(column);
                return rs.wasNull()
                        ? Either.left(new DbError.DecodeError("null value in column: " + column))
                        : Either.right(value);
            } catch (SQLException e) {
                return Either.left(new DbError.DecodeError(e.getMessage()));
            }
        };
    }

    static RowDecoder<Boolean> bool(String column) {
        return rs -> {
            try {
                boolean value = rs.getBoolean(column);
                return rs.wasNull()
                        ? Either.left(new DbError.DecodeError("null value in column: " + column))
                        : Either.right(value);
            } catch (SQLException e) {
                return Either.left(new DbError.DecodeError(e.getMessage()));
            }
        };
    }
}
