package com.dan323.functional.data.sql;

public final class ExprCompiler {

    public static String compile(Expr<?> expr) {

        if (expr instanceof Expr.Column<?> c) {
            return c.tableAlias() + "." + c.name();
        } else if (expr instanceof Expr.Literal<?> l) {
            if (l.value() instanceof String s) {
                return "'" + s + "'";
            }
            return l.value().toString();
        } else if (expr instanceof Expr.Add a) {
            return "(" + compile(a.left()) +
                    " + " + compile(a.right()) + ")";
        } else if (expr instanceof Expr.Gt g) {
            return "(" + compile(g.left()) +
                    " > " + compile(g.right()) + ")";
        } else if (expr instanceof Expr.And a) {
            return "(" + compile(a.left()) +
                    " AND " + compile(a.right()) + ")";
        } else if (expr instanceof Expr.Or a) {
            return "(" + compile(a.left()) +
                    " OR " + compile(a.right()) + ")";
        } else if (expr instanceof Expr.Not n) {
            return "(NOT " + compile(n.value()) + ")";
        } else if (expr instanceof Expr.Sub a) {
            return "(" + compile(a.left()) +
                    " - " + compile(a.right()) + ")";
        } else if (expr instanceof Expr.Lt l) {
            return "(" + compile(l.left()) +
                    " < " + compile(l.right()) + ")";
        } else if (expr instanceof Expr.Mul m) {
            return "(" + compile(m.left()) +
                    " * " + compile(m.right()) + ")";
        } else if (expr instanceof Expr.Lte g) {
            return "(" + compile(g.left()) +
                    " <= " + compile(g.right()) + ")";
        } else if (expr instanceof Expr.Gte g) {
            return "(" + compile(g.left()) +
                    " >= " + compile(g.right()) + ")";
        } else if (expr instanceof Expr.Eq<?> g) {
            return "(" + compile(g.left()) +
                    " = " + compile(g.right()) + ")";
        } else if (expr instanceof Expr.Neq<?> g) {
            return "(" + compile(g.left()) +
                    " <> " + compile(g.right()) + ")";
        } else if (expr instanceof Expr.Div m) {
            return "(" + compile(m.left()) +
                    " / " + compile(m.right()) + ")";
        } else {
            throw new IllegalStateException("Unknown Expr: " + expr);
        }
    }
}
