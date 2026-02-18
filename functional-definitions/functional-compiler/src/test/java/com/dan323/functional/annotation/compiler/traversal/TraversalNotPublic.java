package com.dan323.functional.annotation.compiler.traversal;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;

import java.util.List;
import java.util.function.Function;

@Traversal
public class TraversalNotPublic implements ITraversal<List<?>> {

    private <K, A> K traverse(IApplicative<K> applicative, Function<A, K> fun, List<A> lst) {
        return ApplicativeUtil.pure(applicative, List.of());
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}

