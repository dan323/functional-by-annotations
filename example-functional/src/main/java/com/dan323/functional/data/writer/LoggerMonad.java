package com.dan323.functional.data.writer;

import com.dan323.functional.annotation.Monad;

@Monad
public final class LoggerMonad extends WriterMonad<String> {

    public LoggerMonad() {
        super(StringConcatMonoid.getInstance());
    }
}
