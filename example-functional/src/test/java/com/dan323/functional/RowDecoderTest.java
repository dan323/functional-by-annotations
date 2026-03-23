package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.sql.DbError;
import com.dan323.functional.data.sql.RowDecoder;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RowDecoderTest {

    // -------------------------------------------------------------------------
    // string
    // -------------------------------------------------------------------------

    @Test
    public void stringDecodesColumn() {
        assertEquals(Either.right("hello"), RowDecoder.string("name").decode(stub(Map.of("name", "hello"))));
    }

    @Test
    public void stringReturnsDecodeErrorOnNull() {
        var result = RowDecoder.string("name").decode(stub(Map.of()));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: name")), result);
    }

    @Test
    public void stringReturnsDecodeErrorOnSqlException() {
        var result = RowDecoder.string("name").decode(throwingStub("db error"));
        assertEquals(Either.left(new DbError.DecodeError("db error")), result);
    }

    // -------------------------------------------------------------------------
    // integer
    // -------------------------------------------------------------------------

    @Test
    public void integerDecodesColumn() {
        assertEquals(Either.right(42), RowDecoder.integer("amount").decode(stub(Map.of("amount", 42))));
    }

    @Test
    public void integerReturnsDecodeErrorOnNull() {
        var result = RowDecoder.integer("amount").decode(stub(Map.of()));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: amount")), result);
    }

    @Test
    public void integerReturnsDecodeErrorOnSqlException() {
        var result = RowDecoder.integer("amount").decode(throwingStub("db error"));
        assertEquals(Either.left(new DbError.DecodeError("db error")), result);
    }

    // -------------------------------------------------------------------------
    // longCol
    // -------------------------------------------------------------------------

    @Test
    public void longColDecodesColumn() {
        assertEquals(Either.right(123L), RowDecoder.longCol("id").decode(stub(Map.of("id", 123L))));
    }

    @Test
    public void longColReturnsDecodeErrorOnNull() {
        var result = RowDecoder.longCol("id").decode(stub(Map.of()));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: id")), result);
    }

    @Test
    public void longColReturnsDecodeErrorOnSqlException() {
        var result = RowDecoder.longCol("id").decode(throwingStub("db error"));
        assertEquals(Either.left(new DbError.DecodeError("db error")), result);
    }

    // -------------------------------------------------------------------------
    // bool
    // -------------------------------------------------------------------------

    @Test
    public void boolDecodesColumn() {
        assertEquals(Either.right(true), RowDecoder.bool("active").decode(stub(Map.of("active", true))));
    }

    @Test
    public void boolReturnsDecodeErrorOnNull() {
        var result = RowDecoder.bool("active").decode(stub(Map.of()));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: active")), result);
    }

    @Test
    public void boolReturnsDecodeErrorOnSqlException() {
        var result = RowDecoder.bool("active").decode(throwingStub("db error"));
        assertEquals(Either.left(new DbError.DecodeError("db error")), result);
    }

    // -------------------------------------------------------------------------
    // Combinators
    // -------------------------------------------------------------------------

    @Test
    public void mapTransformsRightValue() {
        var decoder = RowDecoder.map(RowDecoder.integer("n"), x -> x * 2);
        assertEquals(Either.right(10), decoder.decode(stub(Map.of("n", 5))));
    }

    @Test
    public void mapPassesThroughLeft() {
        var decoder = RowDecoder.map(RowDecoder.integer("n"), x -> x * 2);
        var result = decoder.decode(stub(Map.of()));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: n")), result);
    }

    @Test
    public void liftA2CombinesTwoDecoders() {
        var decoder = RowDecoder.liftA2(
                (a, b) -> a + " " + b,
                RowDecoder.string("first"),
                RowDecoder.string("last"));
        assertEquals(Either.right("John Doe"), decoder.decode(stub(Map.of("first", "John", "last", "Doe"))));
    }

    @Test
    public void liftA2ShortCircuitsOnFirstError() {
        var decoder = RowDecoder.liftA2(
                (a, b) -> a + b,
                RowDecoder.string("first"),
                RowDecoder.string("last"));
        // "first" column is absent → left, "last" is present but never reached
        var result = decoder.decode(stub(Map.of("last", "Doe")));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: first")), result);
    }

    @Test
    public void liftA2ReturnsSecondErrorWhenFirstSucceeds() {
        var decoder = RowDecoder.liftA2(
                (a, b) -> a + b,
                RowDecoder.string("first"),
                RowDecoder.string("last"));
        var result = decoder.decode(stub(Map.of("first", "John")));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: last")), result);
    }

    @Test
    public void emptyAlwaysReturnsLeft() {
        assertEquals(
                Either.left(new DbError.DecodeError("Not implemented")),
                RowDecoder.empty().decode(stub(Map.of())));
    }

    // -------------------------------------------------------------------------
    // Stub helpers
    // -------------------------------------------------------------------------

    /** Stubs a ResultSet whose columns are backed by the given map. Missing keys are SQL NULL. */
    private static ResultSet stub(Map<String, Object> values) {
        boolean[] lastWasNull = {false};
        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class[]{ResultSet.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "getString" -> {
                        Object v = values.get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v;
                    }
                    case "getInt" -> {
                        Object v = values.get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v == null ? 0 : ((Number) v).intValue();
                    }
                    case "getLong" -> {
                        Object v = values.get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v == null ? 0L : ((Number) v).longValue();
                    }
                    case "getBoolean" -> {
                        Object v = values.get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v == null ? Boolean.FALSE : v;
                    }
                    case "wasNull" -> lastWasNull[0];
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    /** Stubs a ResultSet that throws SQLException with the given message on any getter. */
    private static ResultSet throwingStub(String message) {
        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class[]{ResultSet.class},
                (proxy, method, args) -> {
                    if (method.getName().startsWith("get")) throw new SQLException(message);
                    return null;
                });
    }
}
