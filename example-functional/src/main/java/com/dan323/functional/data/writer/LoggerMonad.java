package com.dan323.functional.data.writer;

public final class LoggerMonad extends WriterMonad<String> {

    public LoggerMonad() {
        super(StringConcatMonoid.getInstance());
    }
}
