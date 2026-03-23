package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.sql.DbError;
import com.dan323.functional.data.sql.Query;
import com.dan323.functional.data.sql.RowDecoder;
import com.dan323.functional.data.sql.SqlAst;
import com.dan323.functional.data.sql.SqlRunner;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SqlRunnerTest {

    // -------------------------------------------------------------------------
    // Happy paths
    // -------------------------------------------------------------------------

    @Test
    public void runDecodesAllRows() {
        var conn = connectionOver(rows(Map.of("name", "Alice"), Map.of("name", "Bob")));
        var query = new Query<>(new SqlAst.Table("users"), RowDecoder.string("name"));
        assertEquals(Either.right(List.of("Alice", "Bob")), SqlRunner.run(conn, query));
    }

    @Test
    public void runReturnsEmptyListWhenNoRows() {
        var conn = connectionOver(rows());
        var query = new Query<>(new SqlAst.Table("users"), RowDecoder.string("name"));
        assertEquals(Either.right(List.of()), SqlRunner.run(conn, query));
    }

    @Test
    public void runDecodesIntegerColumn() {
        var conn = connectionOver(rows(Map.of("amount", 10), Map.of("amount", 20)));
        var query = new Query<>(new SqlAst.Table("orders"), RowDecoder.integer("amount"));
        assertEquals(Either.right(List.of(10, 20)), SqlRunner.run(conn, query));
    }

    // -------------------------------------------------------------------------
    // Decode errors
    // -------------------------------------------------------------------------

    @Test
    public void runShortCircuitsOnFirstDecodeError() {
        // Second row has "name" missing → decode error; third row is never reached.
        var conn = connectionOver(rows(
                Map.of("name", "Alice"),
                Map.of(),
                Map.of("name", "Charlie")));
        var query = new Query<>(new SqlAst.Table("users"), RowDecoder.string("name"));
        assertEquals(
                Either.left(new DbError.DecodeError("null value in column: name")),
                SqlRunner.run(conn, query));
    }

    @Test
    public void runShortCircuitsOnFirstRowDecodeError() {
        var conn = connectionOver(rows(Map.of()));   // first row already fails
        var query = new Query<>(new SqlAst.Table("users"), RowDecoder.string("name"));
        assertEquals(
                Either.left(new DbError.DecodeError("null value in column: name")),
                SqlRunner.run(conn, query));
    }

    // -------------------------------------------------------------------------
    // SQL exceptions
    // -------------------------------------------------------------------------

    @Test
    public void runReturnsDbErrorWhenExecuteQueryThrows() {
        var conn = throwingConnection("table not found");
        var query = new Query<>(new SqlAst.Table("users"), RowDecoder.string("name"));
        assertEquals(
                Either.left(new DbError.SqlError("table not found")),
                SqlRunner.run(conn, query));
    }

    // -------------------------------------------------------------------------
    // SQL compilation
    // -------------------------------------------------------------------------

    @Test
    public void runPassesCompiledSqlToStatement() {
        String[] captured = {null};
        var conn = connectionCapturing(captured);
        var query = new Query<>(new SqlAst.Table("orders"), RowDecoder.string("id"));
        SqlRunner.run(conn, query);
        assertEquals("SELECT * FROM orders", captured[0]);
    }

    @Test
    public void runCompilesSqlAstBeforeExecution() {
        String[] captured = {null};
        var conn = connectionCapturing(captured);
        var query = new Query<>(
                new SqlAst.Filter(
                        new SqlAst.Table("orders"),
                        new com.dan323.functional.data.sql.Expr.Gt(
                                new com.dan323.functional.data.sql.Expr.Column<>("o", "amount", Number.class),
                                new com.dan323.functional.data.sql.Expr.Literal<>(0, Number.class))),
                RowDecoder.string("id"));
        SqlRunner.run(conn, query);
        assertEquals("SELECT * FROM orders WHERE (o.amount > 0)", captured[0]);
    }

    // -------------------------------------------------------------------------
    // Public API: connection failure
    // -------------------------------------------------------------------------

    @Test
    public void runPublicApiReturnsDbErrorWhenConnectionFails() {
        // No PostgreSQL instance is running at this port — connection must fail.
        var cfg = new com.dan323.functional.data.sql.Config("localhost", 1, "test");
        var query = new Query<>(new SqlAst.Table("t"), RowDecoder.string("x"));
        var result = SqlRunner.run(cfg, query);
        boolean isSqlError = result.either(err -> err instanceof DbError.SqlError, val -> false);
        assertTrue(isSqlError);
    }

    // -------------------------------------------------------------------------
    // Stub helpers
    // -------------------------------------------------------------------------

    @SafeVarargs
    private static ResultSet rows(Map<String, Object>... rowData) {
        int[] index = {-1};
        boolean[] lastWasNull = {false};
        return (ResultSet) Proxy.newProxyInstance(
                ResultSet.class.getClassLoader(),
                new Class[]{ResultSet.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "next" -> {
                        index[0]++;
                        yield index[0] < rowData.length;
                    }
                    case "getString" -> {
                        Object v = rowData[index[0]].get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v;
                    }
                    case "getInt" -> {
                        Object v = rowData[index[0]].get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v == null ? 0 : ((Number) v).intValue();
                    }
                    case "getLong" -> {
                        Object v = rowData[index[0]].get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v == null ? 0L : ((Number) v).longValue();
                    }
                    case "getBoolean" -> {
                        Object v = rowData[index[0]].get(args[0]);
                        lastWasNull[0] = (v == null);
                        yield v == null ? Boolean.FALSE : v;
                    }
                    case "wasNull" -> lastWasNull[0];
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    private static Connection connectionOver(ResultSet rs) {
        var stmt = (Statement) Proxy.newProxyInstance(
                Statement.class.getClassLoader(),
                new Class[]{Statement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "executeQuery" -> rs;
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "createStatement" -> stmt;
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    /** Connection whose Statement throws SQLException on executeQuery. */
    private static Connection throwingConnection(String message) {
        var stmt = (Statement) Proxy.newProxyInstance(
                Statement.class.getClassLoader(),
                new Class[]{Statement.class},
                (proxy, method, args) -> {
                    if ("executeQuery".equals(method.getName())) throw new SQLException(message);
                    return null;
                });
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "createStatement" -> stmt;
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }

    /** Connection that captures the SQL string passed to executeQuery. */
    private static Connection connectionCapturing(String[] target) {
        var stmt = (Statement) Proxy.newProxyInstance(
                Statement.class.getClassLoader(),
                new Class[]{Statement.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "executeQuery" -> {
                        target[0] = (String) args[0];
                        yield rows();
                    }
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class[]{Connection.class},
                (proxy, method, args) -> switch (method.getName()) {
                    case "createStatement" -> stmt;
                    case "close" -> null;
                    default -> throw new UnsupportedOperationException(method.getName());
                });
    }
}
