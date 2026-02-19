package com.dan323.functional.annotation.compiler.traversal;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;

import java.util.Optional;
import java.util.function.Function;

@Traversal
public class TraversalMock implements ITraversal<Optional<?>> {

    public <K, A> K traverse(IApplicative<K> applicative, Function<A, K> fun, Optional<A> elem) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Class<Optional<?>> getClassAtRuntime() {
        return (Class<Optional<?>>) (Class<?>) Optional.class;
    }
}

