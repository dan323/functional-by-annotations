package com.dan323.functional.annotation.compiler.traversal;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;

import java.util.List;
import java.util.function.Function;

@Traversal
// Missing ITraversal implementation - should cause error
public class TraversalNotInterface {

    public <K, A> K traverse(IApplicative<K> applicative, Function<A, K> fun, List<A> lst) {
        return ApplicativeUtil.pure(applicative,List.of());
    }
}

