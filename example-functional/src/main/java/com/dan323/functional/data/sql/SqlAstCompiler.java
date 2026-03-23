package com.dan323.functional.data.sql;

import com.dan323.functional.data.list.List;

public final class SqlAstCompiler {

    private SqlAstCompiler() {}

    private sealed interface Fragment permits Fragment.Query, Fragment.Union {
        record Query(String select, String from, String where) implements Fragment {}
        record Union(Fragment left, Fragment right) implements Fragment {}
    }

    public static String compile(SqlAst ast) {
        return render(toFragment(ast));
    }

    private static String render(Fragment fragment) {
        if (fragment instanceof Fragment.Query q) {
            StringBuilder sb = new StringBuilder("SELECT ").append(q.select());
            if (q.from() != null) sb.append(" FROM ").append(q.from());
            if (q.where() != null) sb.append(" WHERE ").append(q.where());
            return sb.toString();
        } else if (fragment instanceof Fragment.Union u) {
            return "(" + render(u.left()) + ") UNION (" + render(u.right()) + ")";
        }
        throw new IllegalStateException("Unknown Fragment: " + fragment);
    }

    private static Fragment toFragment(SqlAst ast) {
        if (ast instanceof SqlAst.Empty) {
            return new Fragment.Query("1", null, "FALSE");
        } else if (ast instanceof SqlAst.Pure) {
            return new Fragment.Query("1", null, null);
        } else if (ast instanceof SqlAst.Table t) {
            return new Fragment.Query("*", t.name(), null);
        } else if (ast instanceof SqlAst.Filter f) {
            Fragment src = toFragment(f.source());
            String newCond = ExprCompiler.compile(f.condition());
            if (src instanceof Fragment.Query q) {
                String combined = q.where() == null ? newCond : "(" + q.where() + ") AND (" + newCond + ")";
                return new Fragment.Query(q.select(), q.from(), combined);
            }
            return new Fragment.Query("*", "(" + render(src) + ")", newCond);
        } else if (ast instanceof SqlAst.Project p) {
            Fragment src = toFragment(p.source());
            String cols = joinExprs(p.exprList());
            if (src instanceof Fragment.Query q && "*".equals(q.select())) {
                return new Fragment.Query(cols, q.from(), q.where());
            }
            return new Fragment.Query(cols, "(" + render(src) + ")", null);
        } else if (ast instanceof SqlAst.Join j) {
            String leftSrc = asJoinSource(toFragment(j.left()));
            String rightSrc = asJoinSource(toFragment(j.right()));
            String from = j.on() == null
                    ? leftSrc + " CROSS JOIN " + rightSrc
                    : leftSrc + " JOIN " + rightSrc + " ON " + ExprCompiler.compile(j.on());
            return new Fragment.Query("*", from, null);
        } else if (ast instanceof SqlAst.Product p) {
            String leftSrc = asJoinSource(toFragment(p.left()));
            String rightSrc = asJoinSource(toFragment(p.right()));
            return new Fragment.Query("*", leftSrc + " CROSS JOIN " + rightSrc, null);
        } else if (ast instanceof SqlAst.Union u) {
            return new Fragment.Union(toFragment(u.left()), toFragment(u.right()));
        }
        throw new IllegalStateException("Unknown SqlAst: " + ast);
    }

    // A fragment can be used directly as a JOIN source only when it has no WHERE
    // and no custom projection — otherwise it must be wrapped as a subquery.
    private static String asJoinSource(Fragment fragment) {
        if (fragment instanceof Fragment.Query q && q.where() == null && "*".equals(q.select())) {
            return q.from();
        }
        return "(" + render(fragment) + ")";
    }

    private static String joinExprs(List<Expr<?>> exprs) {
        return exprs.head().maybe(
                h -> {
                    String tail = joinExprs(exprs.tail());
                    String compiled = ExprCompiler.compile(h);
                    return tail.isEmpty() ? compiled : compiled + ", " + tail;
                },
                "");
    }
}
