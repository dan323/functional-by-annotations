package com.dan323.functional.data.sql;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

/**
 * Monoid instance for {@link SqlAst}.
 *
 * <ul>
 *   <li>{@code unit()} is the empty query — the identity element.</li>
 *   <li>{@code op(a, b)} is {@code UNION} — combines two queries into one.</li>
 * </ul>
 *
 * This lets you fold a collection of queries into a single query:
 * {@code queries.stream().reduce(SqlAstMonoid.unit(), SqlAstMonoid::op)}.
 */
@Monoid
public final class SqlAstMonoid implements IMonoid<SqlAst> {

    private SqlAstMonoid() {}

    private static final SqlAstMonoid INSTANCE = new SqlAstMonoid();

    public static SqlAstMonoid getInstance() {
        return INSTANCE;
    }

    public static SqlAst op(SqlAst left, SqlAst right) {
        return new SqlAst.Union(left, right);
    }

    public static SqlAst unit() {
        return new SqlAst.Empty();
    }
}
