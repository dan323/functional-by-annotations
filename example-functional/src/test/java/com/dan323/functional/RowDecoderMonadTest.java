package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.sql.DbError;
import com.dan323.functional.data.sql.RowDecoder;
import com.dan323.functional.data.sql.RowDecoderMonad;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RowDecoderMonadTest {

    // -------------------------------------------------------------------------
    // pure
    // -------------------------------------------------------------------------

    @Test
    public void pureIgnoresResultSetAndReturnsRight() {
        assertEquals(Either.right(42), RowDecoderMonad.pure(42).decode(null));
    }

    // -------------------------------------------------------------------------
    // flatMap — the key operation
    // -------------------------------------------------------------------------

    @Test
    public void flatMapChainsDecodersUsingFirstResult() {
        // Decode a name, then build a greeting that also reads a title column.
        var decoder = RowDecoderMonad.flatMap(
                name -> RowDecoderMonad.map(RowDecoder.string("title"), title -> title + " " + name),
                RowDecoder.string("name"));
        var result = decoder.decode(stub(Map.of("name", "Smith", "title", "Dr.")));
        assertEquals(Either.right("Dr. Smith"), result);
    }

    @Test
    public void flatMapShortCircuitsWhenFirstDecoderFails() {
        var decoder = RowDecoderMonad.flatMap(
                name -> RowDecoder.string("title"),
                RowDecoder.string("name"));
        // "name" absent → first decoder fails; second is never invoked
        var result = decoder.decode(stub(Map.of("title", "Dr.")));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: name")), result);
    }

    @Test
    public void flatMapPropagatesSecondDecoderFailure() {
        var decoder = RowDecoderMonad.flatMap(
                name -> RowDecoder.string("title"),
                RowDecoder.string("name"));
        // "name" present, "title" absent → second decoder fails
        var result = decoder.decode(stub(Map.of("name", "Smith")));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: title")), result);
    }

    // -------------------------------------------------------------------------
    // disjunction — fallback decoders
    // -------------------------------------------------------------------------

    @Test
    public void disjunctionReturnsFirstWhenItSucceeds() {
        var decoder = RowDecoderMonad.disjunction(
                RowDecoder.string("preferred_name"),
                RowDecoder.string("name"));
        var result = decoder.decode(stub(Map.of("preferred_name", "Bob", "name", "Robert")));
        assertEquals(Either.right("Bob"), result);
    }

    @Test
    public void disjunctionFallsBackToSecondWhenFirstFails() {
        var decoder = RowDecoderMonad.disjunction(
                RowDecoder.string("preferred_name"),
                RowDecoder.string("name"));
        // "preferred_name" absent → falls back to "name"
        var result = decoder.decode(stub(Map.of("name", "Robert")));
        assertEquals(Either.right("Robert"), result);
    }

    @Test
    public void disjunctionReturnsSecondErrorWhenBothFail() {
        var decoder = RowDecoderMonad.disjunction(
                RowDecoder.string("preferred_name"),
                RowDecoder.string("name"));
        var result = decoder.decode(stub(Map.of()));
        assertEquals(Either.left(new DbError.DecodeError("null value in column: name")), result);
    }

    // -------------------------------------------------------------------------
    // empty
    // -------------------------------------------------------------------------

    @Test
    public void emptyAlwaysFails() {
        assertEquals(
                Either.left(new DbError.DecodeError("Not implemented")),
                RowDecoderMonad.<String>empty().decode(null));
    }

    // -------------------------------------------------------------------------
    // fapply
    // -------------------------------------------------------------------------

    @Test
    public void fapplyAppliesFunctionDecoder() {
        java.util.function.Function<String, String> toUpper = String::toUpperCase;
        var decF = RowDecoderMonad.pure(toUpper);
        var decA = RowDecoder.string("name");
        var result = RowDecoderMonad.fapply(decF, decA).decode(stub(Map.of("name", "alice")));
        assertEquals(Either.right("ALICE"), result);
    }

    @Test
    public void fapplyShortCircuitsWhenFunctionDecoderFails() {
        // Explicit lambda avoids functional-interface-within-functional-interface inference ambiguity.
        RowDecoder<java.util.function.Function<String, String>> decF =
                rs -> Either.left(new DbError.DecodeError("no function"));
        var decA = RowDecoder.string("name");
        var result = RowDecoderMonad.fapply(decF, decA).decode(stub(Map.of("name", "alice")));
        assertEquals(Either.left(new DbError.DecodeError("no function")), result);
    }

    // -------------------------------------------------------------------------
    // Stub helper
    // -------------------------------------------------------------------------

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
                    case "wasNull" -> lastWasNull[0];
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
