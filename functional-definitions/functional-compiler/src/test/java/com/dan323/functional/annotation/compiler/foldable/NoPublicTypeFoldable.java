package com.dan323.functional.annotation.compiler.foldable;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.funcs.IFoldable;

import java.util.Optional;
import java.util.function.BiFunction;

@Foldable
class NoPublicTypeFoldable implements IFoldable<Optional<?>> {

    public <A, B> B foldr(BiFunction<A, B, B> function, B b, Optional<A> opt) {
        return opt.map(y -> function.apply(y, b)).orElse(b);
    }

    @Override
    public Class<Optional<?>> getClassAtRuntime() {
        return (Class<Optional<?>>)(Class) Optional.class;
    }
}
