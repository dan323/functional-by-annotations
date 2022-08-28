package com.dan323.functional.foldable;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.funcs.IFoldable;
import com.dan323.functional.annotation.util.MonoidUtil;
import com.dan323.functional.annotation.util.SemigroupUtil;

import java.util.List;
import java.util.function.Function;

@Foldable
public class FoldableWithFold implements IFoldable<List<?>> {

    private FoldableWithFold() {
    }

    public static final FoldableWithFold LIST_FOLD = new FoldableWithFold();

    public <A, M> M foldMap(IMonoid<M> monoid, Function<A, M> function, List<A> lst) {
        return lst.stream().map(function).reduce(MonoidUtil.unit(monoid), (x, y) -> SemigroupUtil.op(monoid, x, y));
    }

    public <A> A fold(IMonoid<A> monoid, List<A> lst) {
        return MonoidUtil.unit(monoid);
    }
}
