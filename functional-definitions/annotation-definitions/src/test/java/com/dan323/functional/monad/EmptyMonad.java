package com.dan323.functional.monad;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.List;

@Monad
public final class EmptyMonad implements IMonad<List<?>> {

    private EmptyMonad(){

    }

    public static final EmptyMonad MONAD = new EmptyMonad();
}
