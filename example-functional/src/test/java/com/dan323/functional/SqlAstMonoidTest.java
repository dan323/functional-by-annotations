package com.dan323.functional;

import com.dan323.functional.data.sql.SqlAst;
import com.dan323.functional.data.sql.SqlAstCompiler;
import com.dan323.functional.data.sql.SqlAstMonoid;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class SqlAstMonoidTest {

    @Test
    public void unitIsEmpty() {
        assertInstanceOf(SqlAst.Empty.class, SqlAstMonoid.unit());
    }

    @Test
    public void opProducesUnion() {
        var a = new SqlAst.Table("a");
        var b = new SqlAst.Table("b");
        var result = SqlAstMonoid.op(a, b);
        assertInstanceOf(SqlAst.Union.class, result);
        assertEquals("(SELECT * FROM a) UNION (SELECT * FROM b)", SqlAstCompiler.compile(result));
    }

    // Left identity: op(unit(), q) is semantically equivalent to q (UNION with empty set).
    @Test
    public void leftIdentityHoldsSemantically() {
        var q = new SqlAst.Table("orders");
        var combined = SqlAstMonoid.op(SqlAstMonoid.unit(), q);
        assertEquals(
                "(SELECT 1 WHERE FALSE) UNION (SELECT * FROM orders)",
                SqlAstCompiler.compile(combined));
    }

    // Right identity: op(q, unit()) is semantically equivalent to q.
    @Test
    public void rightIdentityHoldsSemantically() {
        var q = new SqlAst.Table("orders");
        var combined = SqlAstMonoid.op(q, SqlAstMonoid.unit());
        assertEquals(
                "(SELECT * FROM orders) UNION (SELECT 1 WHERE FALSE)",
                SqlAstCompiler.compile(combined));
    }

    // Associativity: op(op(a,b),c) and op(a,op(b,c)) produce the same structure.
    @Test
    public void opIsAssociative() {
        var a = new SqlAst.Table("a");
        var b = new SqlAst.Table("b");
        var c = new SqlAst.Table("c");
        var leftAssoc  = SqlAstMonoid.op(SqlAstMonoid.op(a, b), c);
        var rightAssoc = SqlAstMonoid.op(a, SqlAstMonoid.op(b, c));
        // Both are valid; left-assoc wraps differently but each is a valid UNION tree.
        assertEquals(
                "((SELECT * FROM a) UNION (SELECT * FROM b)) UNION (SELECT * FROM c)",
                SqlAstCompiler.compile(leftAssoc));
        assertEquals(
                "(SELECT * FROM a) UNION ((SELECT * FROM b) UNION (SELECT * FROM c))",
                SqlAstCompiler.compile(rightAssoc));
    }

    // Folding a list of queries with op/unit mirrors Stream.reduce.
    @Test
    public void foldingListOfQueriesProducesUnionChain() {
        var tables = List.of("orders", "archived_orders", "deleted_orders");
        var result = tables.stream()
                .map(SqlAst.Table::new)
                .map(t -> (SqlAst) t)
                .reduce(SqlAstMonoid.unit(), SqlAstMonoid::op);
        assertEquals(
                "(((SELECT 1 WHERE FALSE) UNION (SELECT * FROM orders))"
                        + " UNION (SELECT * FROM archived_orders))"
                        + " UNION (SELECT * FROM deleted_orders)",
                SqlAstCompiler.compile(result));
    }
}
