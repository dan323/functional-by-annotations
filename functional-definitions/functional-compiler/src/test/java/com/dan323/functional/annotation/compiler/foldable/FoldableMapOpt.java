package com.dan323.functional.annotation.compiler.foldable;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.funcs.IFoldable;
import com.dan323.functional.annotation.util.MonoidUtil;

import java.util.Optional;
import java.util.function.Function;

@Foldable
public class FoldableMapOpt implements IFoldable<Optional<?>> {

    public <A, M> M foldMap(IMonoid<M> monoid, Function<A, M> function, Optional<A> opt) {
        return opt.map(function).orElse(MonoidUtil.unit(monoid));
    }
}
