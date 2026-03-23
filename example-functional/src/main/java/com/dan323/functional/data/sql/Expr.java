package com.dan323.functional.data.sql;

public sealed interface Expr<A>
        permits Expr.Column,
        Expr.Literal,
        Expr.Add,
        Expr.Sub,
        Expr.Mul,
        Expr.Div,
        Expr.Eq,
        Expr.Neq,
        Expr.Gt,
        Expr.Lt,
        Expr.Gte,
        Expr.Lte,
        Expr.And,
        Expr.Or,
        Expr.Not {

    record Column<A>(
            String tableAlias,
            String name,
            Class<A> type
    ) implements Expr<A> {}

    record Literal<A>(
            A value,
            Class<A> type
    ) implements Expr<A> {}

    record Add(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Number> {}

    record Sub(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Number> {}

    record Mul(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Number> {}

    record Div(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Number> {}

    record Eq<A>(
            Expr<A> left,
            Expr<A> right
    ) implements Expr<Boolean> {}

    record Neq<A>(
            Expr<A> left,
            Expr<A> right
    ) implements Expr<Boolean> {}

    record Gt(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Boolean> {}

    record Lt(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Boolean> {}

    record Gte(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Boolean> {}

    record Lte(
            Expr<Number> left,
            Expr<Number> right
    ) implements Expr<Boolean> {}
    
    record And(
            Expr<Boolean> left,
            Expr<Boolean> right
    ) implements Expr<Boolean> {}
    
    record Or(
            Expr<Boolean> left,
            Expr<Boolean> right
    ) implements Expr<Boolean> {}
    
    record Not(
            Expr<Boolean> value
    ) implements Expr<Boolean> {}
}