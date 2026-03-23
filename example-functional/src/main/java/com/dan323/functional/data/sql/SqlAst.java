package com.dan323.functional.data.sql;

import com.dan323.functional.data.list.List;

public sealed interface SqlAst permits SqlAst.Empty, SqlAst.Table, SqlAst.Product, SqlAst.Join,
        SqlAst.Filter, SqlAst.Union, SqlAst.Pure, SqlAst.Project {

    record Table(String name) implements SqlAst {}

    record Product(SqlAst left, SqlAst right) implements SqlAst {}

    record Join(SqlAst left, SqlAst right, Expr<Boolean> on) implements SqlAst {}

    record Filter(SqlAst source, Expr<Boolean> condition) implements SqlAst {}

    record Union(SqlAst left, SqlAst right) implements SqlAst {}

    record Empty() implements SqlAst {}

    record Pure() implements SqlAst {}

    record Project(List<Expr<?>> exprList, SqlAst source) implements SqlAst {}
}
