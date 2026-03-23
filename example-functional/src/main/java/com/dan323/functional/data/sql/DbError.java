package com.dan323.functional.data.sql;

public sealed interface DbError permits DbError.SqlError, DbError.DecodeError {

    record SqlError(String message) implements DbError {}

    record DecodeError(String message) implements DbError {}
}