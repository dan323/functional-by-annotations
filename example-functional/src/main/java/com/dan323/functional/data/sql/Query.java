package com.dan323.functional.data.sql;

public record Query<A>(SqlAst sql, RowDecoder<A> decoder){
}
