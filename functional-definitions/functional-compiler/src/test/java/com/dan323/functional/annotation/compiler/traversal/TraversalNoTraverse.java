package com.dan323.functional.annotation.compiler.traversal;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.funcs.ITraversal;

import java.util.List;

@Traversal
public class TraversalNoTraverse implements ITraversal<List<?>> {

    // Missing traverse and sequenceA methods - should cause error

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}

