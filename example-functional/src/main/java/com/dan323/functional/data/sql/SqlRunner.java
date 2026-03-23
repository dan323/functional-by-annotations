package com.dan323.functional.data.sql;

import com.dan323.functional.data.either.Either;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class SqlRunner {

    private SqlRunner() {}

    public static <A> Either<DbError, List<A>> run(Config cfg, Query<A> query) {
        try (var conn = DriverManager.getConnection(Config.getPath.apply(cfg))) {
            return run(conn, query);
        } catch (SQLException e) {
            return Either.left(new DbError.SqlError(e.getMessage()));
        }
    }

    public static <A> Either<DbError, List<A>> run(Connection conn, Query<A> query) {
        var sql = SqlAstCompiler.compile(query.sql());
        try (var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            var results = new ArrayList<A>();
            while (rs.next()) {
                DbError error = query.decoder().decode(rs).either(Function.identity(), val -> {
                    results.add(val);
                    return null;
                });
                if (error != null) {
                    return Either.left(error);
                }
            }
            return Either.right(results);
        } catch (SQLException e) {
            return Either.left(new DbError.SqlError(e.getMessage()));
        }
    }
}
