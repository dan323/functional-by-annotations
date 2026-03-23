package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.sql.Expr;
import com.dan323.functional.data.sql.Query;
import com.dan323.functional.data.sql.QueryFunctor;
import com.dan323.functional.data.sql.RowDecoder;
import com.dan323.functional.data.sql.SqlAst;
import com.dan323.functional.data.sql.SqlAstCompiler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryFunctorTest {

    @Test
    public void pureProducesSelectOneAndIgnoresResultSet() {
        var query = QueryFunctor.pure("hello");
        assertEquals("SELECT 1", SqlAstCompiler.compile(query.sql()));
        assertEquals(Either.right("hello"), query.decoder().decode(null));
    }

    @Test
    public void emptyProducesSelectOneWhereFalse() {
        assertEquals("SELECT 1 WHERE FALSE", SqlAstCompiler.compile(QueryFunctor.empty().sql()));
    }

    @Test
    public void mapPreservesSqlAndTransformsDecoder() {
        var base = QueryFunctor.pure(5);
        var mapped = QueryFunctor.map(base, x -> x * 2);
        assertEquals(SqlAstCompiler.compile(base.sql()), SqlAstCompiler.compile(mapped.sql()));
        assertEquals(Either.right(10), mapped.decoder().decode(null));
    }

    @Test
    public void disjunctionProducesUnionSql() {
        var qa = new Query<>(new SqlAst.Table("archived"), RowDecoder.string("name"));
        var qb = new Query<>(new SqlAst.Table("active"), RowDecoder.string("name"));
        assertEquals(
                "(SELECT * FROM archived) UNION (SELECT * FROM active)",
                SqlAstCompiler.compile(QueryFunctor.disjunction(qa, qb).sql()));
    }

    // liftA2 must use CROSS JOIN (Product), not an unconditional Join with null.
    @Test
    public void liftA2UsesCrossJoin() {
        var qa = QueryFunctor.pure(1);
        var qb = QueryFunctor.pure(2);
        var qc = QueryFunctor.liftA2(Integer::sum, qa, qb);
        assertEquals(
                "SELECT * FROM (SELECT 1) CROSS JOIN (SELECT 1)",
                SqlAstCompiler.compile(qc.sql()));
        assertEquals(Either.right(3), qc.decoder().decode(null));
    }

    // join must produce a JOIN … ON … in the SQL.
    @Test
    public void joinUsesJoinWithCondition() {
        var qa = new Query<>(new SqlAst.Table("orders"), RowDecoder.string("id"));
        var qb = new Query<>(new SqlAst.Table("customers"), RowDecoder.string("name"));
        var on = new Expr.Eq<>(
                new Expr.Column<>("o", "id", Integer.class),
                new Expr.Column<>("c", "id", Integer.class));
        var qc = QueryFunctor.join((id, name) -> id + "-" + name, qa, qb, on);
        assertEquals(
                "SELECT * FROM orders JOIN customers ON (o.id = c.id)",
                SqlAstCompiler.compile(qc.sql()));
    }
}
