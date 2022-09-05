package com.dan323.functional.util.foldable;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.compiler.util.SemigroupUtil;
import com.dan323.functional.annotation.funcs.IFoldable;

import java.util.List;
import java.util.function.Function;

@Foldable
public class ListFoldFoldMap implements IFoldable<List<?>> {

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }

    private ListFoldFoldMap(){}

    public static final ListFoldFoldMap LIST_FOLD = new ListFoldFoldMap();

    public <A, M> M foldMap(IMonoid<M> monoid, Function<A, M> function, List<A> lst) {
        return lst.stream().map(function).reduce(MonoidUtil.unit(monoid), (x, y) -> SemigroupUtil.op(monoid, x, y));
    }
}
