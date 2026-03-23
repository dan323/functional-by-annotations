package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.sql.Expr;
import com.dan323.functional.data.sql.SqlAst;
import com.dan323.functional.data.sql.SqlAstCompiler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqlAstCompilerTest {

    // -------------------------------------------------------------------------
    // Leaf nodes
    // -------------------------------------------------------------------------

    @Test
    public void empty() {
        assertEquals("SELECT 1 WHERE FALSE", SqlAstCompiler.compile(new SqlAst.Empty()));
    }

    @Test
    public void pure() {
        assertEquals("SELECT 1", SqlAstCompiler.compile(new SqlAst.Pure()));
    }

    @Test
    public void table() {
        assertEquals("SELECT * FROM orders", SqlAstCompiler.compile(new SqlAst.Table("orders")));
    }

    // -------------------------------------------------------------------------
    // Filter — folding paths
    // -------------------------------------------------------------------------

    // Filter on a plain table: WHERE is set for the first time.
    @Test
    public void filterOnTable() {
        var ast = new SqlAst.Filter(
                new SqlAst.Table("orders", "o"),
                new Expr.Gt(col("o", "amount"), lit(100)));
        assertEquals("SELECT * FROM orders AS o WHERE (o.amount > 100)", SqlAstCompiler.compile(ast));
    }

    // Two consecutive filters are folded into a single WHERE … AND …, no subquery.
    @Test
    public void twoFiltersAreFlattenedWithAnd() {
        var ast = new SqlAst.Filter(
                new SqlAst.Filter(
                        new SqlAst.Table("orders", "o"),
                        new Expr.Gt(col("o", "amount"), lit(50))),
                new Expr.Lt(col("o", "amount"), lit(200)));
        assertEquals(
                "SELECT * FROM orders AS o WHERE ((o.amount > 50)) AND ((o.amount < 200))",
                SqlAstCompiler.compile(ast));
    }

    // Three consecutive filters fold into a single WHERE clause.
    @Test
    public void threeFiltersAreFlattenedIntoOneWhere() {
        var ast = new SqlAst.Filter(
                new SqlAst.Filter(
                        new SqlAst.Filter(
                                new SqlAst.Table("orders", "o"),
                                new Expr.Gt(col("o", "id"), lit(0))),
                        new Expr.Gt(col("o", "amount"), lit(0))),
                new Expr.Lt(col("o", "amount"), lit(100)));
        assertEquals(
                "SELECT * FROM orders AS o WHERE (((o.id > 0)) AND ((o.amount > 0))) AND ((o.amount < 100))",
                SqlAstCompiler.compile(ast));
    }

    // Filter applied to a Union must wrap — the WHERE cannot fold into either branch.
    @Test
    public void filterOnUnionWrapsSubquery() {
        var union = new SqlAst.Union(new SqlAst.Table("a"), new SqlAst.Table("b"));
        var ast = new SqlAst.Filter(
                union,
                new Expr.Eq<>(colInt("t", "x"), litInt(1)));
        assertEquals(
                "SELECT * FROM ((SELECT * FROM a) UNION (SELECT * FROM b)) WHERE (t.x = 1)",
                SqlAstCompiler.compile(ast));
    }

    // -------------------------------------------------------------------------
    // Project — folding paths
    // -------------------------------------------------------------------------

    // Project on a plain table: SELECT columns replace the default *.
    @Test
    public void project() {
        var ast = new SqlAst.Project(twoColumns(), new SqlAst.Table("orders", "o"));
        assertEquals("SELECT o.id, o.name FROM orders AS o", SqlAstCompiler.compile(ast));
    }

    // Project on Filter: both fold into one query (the motivating optimisation).
    @Test
    public void projectOnFilterIsFlattened() {
        var ast = new SqlAst.Project(
                twoColumns(),
                new SqlAst.Filter(
                        new SqlAst.Table("orders", "o"),
                        new Expr.Lt(col("o", "amount"), lit(4))));
        assertEquals(
                "SELECT o.id, o.name FROM orders AS o WHERE (o.amount < 4)",
                SqlAstCompiler.compile(ast));
    }

    // Filter on Project: both fold into one query (the motivating optimisation).
    @Test
    public void filterOnProjectIsFlattened() {
        var ast = new SqlAst.Filter(
                new SqlAst.Project(twoColumns(), new SqlAst.Table("orders", "o")),
                new Expr.Lt(col("o", "amount"), lit(4)));
        assertEquals(
                "SELECT o.id, o.name FROM orders AS o WHERE (o.amount < 4)",
                SqlAstCompiler.compile(ast));
    }

    // Filter + Project + Filter all collapse into a single SELECT … FROM … WHERE.
    @Test
    public void filterProjectFilterAllFoldIntoOneQuery() {
        var ast = new SqlAst.Filter(
                new SqlAst.Project(
                        twoColumns(),
                        new SqlAst.Filter(
                                new SqlAst.Table("orders", "o"),
                                new Expr.Gt(col("o", "amount"), lit(0)))),
                new Expr.Lt(col("o", "amount"), lit(4)));
        assertEquals(
                "SELECT o.id, o.name FROM orders AS o WHERE ((o.amount > 0)) AND ((o.amount < 4))",
                SqlAstCompiler.compile(ast));
    }

    // Project on an already-projected query cannot fold (select ≠ *) — wraps subquery.
    @Test
    public void projectOnProjectWrapsSubquery() {
        var ast = new SqlAst.Project(
                oneColumn(),
                new SqlAst.Project(twoColumns(), new SqlAst.Table("orders", "o")));
        assertEquals(
                "SELECT o.id FROM (SELECT o.id, o.name FROM orders AS o)",
                SqlAstCompiler.compile(ast));
    }

    // Project on a Union must wrap — the Union fragment is not a Query.
    @Test
    public void projectOnUnionWrapsSubquery() {
        var ast = new SqlAst.Project(
                oneColumn(),
                new SqlAst.Union(new SqlAst.Table("a"), new SqlAst.Table("b")));
        assertEquals(
                "SELECT o.id FROM ((SELECT * FROM a) UNION (SELECT * FROM b))",
                SqlAstCompiler.compile(ast));
    }

    // After wrapping via Project-on-Union, a subsequent Filter still folds in.
    @Test
    public void filterOnProjectOnUnionFoldsIntoProjectedQuery() {
        var ast = new SqlAst.Filter(
                new SqlAst.Project(
                        oneColumn(),
                        new SqlAst.Union(new SqlAst.Table("a"), new SqlAst.Table("b"))),
                new Expr.Eq<>(colInt("t", "x"), litInt(1)));
        assertEquals(
                "SELECT o.id FROM ((SELECT * FROM a) UNION (SELECT * FROM b)) WHERE (t.x = 1)",
                SqlAstCompiler.compile(ast));
    }

    // -------------------------------------------------------------------------
    // Join
    // -------------------------------------------------------------------------

    // Both sides are plain tables: no subqueries in the FROM clause.
    @Test
    public void joinTwoTables() {
        var ast = new SqlAst.Join(
                new SqlAst.Table("orders", "o"),
                new SqlAst.Table("customers", "c"),
                new Expr.Eq<>(colInt("o", "customer_id"), colInt("c", "id")));
        assertEquals(
                "SELECT * FROM orders AS o JOIN customers AS c ON (o.customer_id = c.id)",
                SqlAstCompiler.compile(ast));
    }

    @Test
    public void joinWithNullConditionIsCrossJoin() {
        assertEquals(
                "SELECT * FROM a CROSS JOIN b",
                SqlAstCompiler.compile(new SqlAst.Join(new SqlAst.Table("a"), new SqlAst.Table("b"), null)));
    }

    // One filtered side is wrapped as a subquery; the other bare table is not.
    @Test
    public void joinWithOneFilteredSideWrapsSubquery() {
        var ast = new SqlAst.Join(
                new SqlAst.Filter(
                        new SqlAst.Table("orders", "o"),
                        new Expr.Gt(col("o", "amount"), lit(0))),
                new SqlAst.Table("customers", "c"),
                new Expr.Eq<>(colInt("o", "customer_id"), colInt("c", "id")));
        assertEquals(
                "SELECT * FROM (SELECT * FROM orders AS o WHERE (o.amount > 0)) JOIN customers AS c ON (o.customer_id = c.id)",
                SqlAstCompiler.compile(ast));
    }

    // Both filtered sides are independently wrapped.
    @Test
    public void joinWithBothSidesFilteredWrapsBothSubqueries() {
        var ast = new SqlAst.Join(
                new SqlAst.Filter(new SqlAst.Table("orders", "o"), new Expr.Gt(col("o", "amount"), lit(0))),
                new SqlAst.Filter(new SqlAst.Table("customers", "c"), new Expr.Gt(col("c", "id"), lit(0))),
                new Expr.Eq<>(colInt("o", "customer_id"), colInt("c", "id")));
        assertEquals(
                "SELECT * FROM (SELECT * FROM orders AS o WHERE (o.amount > 0))"
                        + " JOIN (SELECT * FROM customers AS c WHERE (c.id > 0))"
                        + " ON (o.customer_id = c.id)",
                SqlAstCompiler.compile(ast));
    }

    // Filter on a Join result folds into the join — no wrapping needed.
    @Test
    public void filterOnJoinFoldsIntoJoin() {
        var ast = new SqlAst.Filter(
                new SqlAst.Join(
                        new SqlAst.Table("orders", "o"),
                        new SqlAst.Table("customers", "c"),
                        new Expr.Eq<>(colInt("o", "customer_id"), colInt("c", "id"))),
                new Expr.Gt(col("o", "amount"), lit(0)));
        assertEquals(
                "SELECT * FROM orders AS o JOIN customers AS c ON (o.customer_id = c.id) WHERE (o.amount > 0)",
                SqlAstCompiler.compile(ast));
    }

    // Project on a Join result folds in — replaces SELECT *.
    @Test
    public void projectOnJoinFoldsIntoJoin() {
        var ast = new SqlAst.Project(
                twoColumns(),
                new SqlAst.Join(
                        new SqlAst.Table("orders", "o"),
                        new SqlAst.Table("customers", "c"),
                        new Expr.Eq<>(colInt("o", "customer_id"), colInt("c", "id"))));
        assertEquals(
                "SELECT o.id, o.name FROM orders AS o JOIN customers AS c ON (o.customer_id = c.id)",
                SqlAstCompiler.compile(ast));
    }

    // -------------------------------------------------------------------------
    // Product
    // -------------------------------------------------------------------------

    @Test
    public void productOfTwoTables() {
        assertEquals(
                "SELECT * FROM a CROSS JOIN b",
                SqlAstCompiler.compile(new SqlAst.Product(new SqlAst.Table("a"), new SqlAst.Table("b"))));
    }

    // A filtered table used in a Product is wrapped as a subquery.
    @Test
    public void productWithFilteredSideWrapsSubquery() {
        var ast = new SqlAst.Product(
                new SqlAst.Filter(new SqlAst.Table("orders", "o"), new Expr.Gt(col("o", "amount"), lit(0))),
                new SqlAst.Table("customers"));
        assertEquals(
                "SELECT * FROM (SELECT * FROM orders AS o WHERE (o.amount > 0)) CROSS JOIN customers",
                SqlAstCompiler.compile(ast));
    }

    // -------------------------------------------------------------------------
    // Union
    // -------------------------------------------------------------------------

    @Test
    public void unionOfTwoTables() {
        var ast = new SqlAst.Union(new SqlAst.Table("archived_orders"), new SqlAst.Table("orders"));
        assertEquals(
                "(SELECT * FROM archived_orders) UNION (SELECT * FROM orders)",
                SqlAstCompiler.compile(ast));
    }

    // Union whose branches are themselves non-trivial queries.
    @Test
    public void unionOfFilteredTables() {
        var ast = new SqlAst.Union(
                new SqlAst.Filter(new SqlAst.Table("archived_orders", "o"), new Expr.Gt(col("o", "amount"), lit(0))),
                new SqlAst.Filter(new SqlAst.Table("orders", "o"), new Expr.Gt(col("o", "amount"), lit(0))));
        assertEquals(
                "(SELECT * FROM archived_orders AS o WHERE (o.amount > 0)) UNION (SELECT * FROM orders AS o WHERE (o.amount > 0))",
                SqlAstCompiler.compile(ast));
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static Expr.Column<Number> col(String alias, String name) {
        return new Expr.Column<>(alias, name, Number.class);
    }

    private static Expr.Column<Integer> colInt(String alias, String name) {
        return new Expr.Column<>(alias, name, Integer.class);
    }

    private static Expr.Literal<Number> lit(int value) {
        return new Expr.Literal<>(value, Number.class);
    }

    private static Expr.Literal<Integer> litInt(int value) {
        return new Expr.Literal<>(value, Integer.class);
    }

    private static FiniteList<Expr<?>> oneColumn() {
        return FiniteList.cons(new Expr.Column<>("o", "id", Integer.class), List.nil());
    }

    private static FiniteList<Expr<?>> twoColumns() {
        return FiniteList.cons(
                new Expr.Column<>("o", "id", Integer.class),
                FiniteList.cons(new Expr.Column<>("o", "name", String.class), List.nil()));
    }
}
