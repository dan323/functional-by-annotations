package com.dan323.functional.util.traversal;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;
import com.dan323.functional.util.foldable.ListFoldFoldr;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Traversal
public class TraversalSequenceA implements ITraversal<List<?>> {

    public <K> K sequenceA(IApplicative<K> applicative, List<K> lst) {
        ListFoldFoldr listFoldFoldr = new ListFoldFoldr();
        return listFoldFoldr.foldr((K k1, K k2) -> ApplicativeUtil.liftA2(applicative, this::cons, k1, k2), ApplicativeUtil.pure(applicative, List.of()), lst);
    }

    public <A,B> List<B> map(List<A> base, Function<A,B> function){
        return base.stream().map(function).toList();
    }

    private List<Object> cons(Object a, List<Object> lst) {
        var sol = new ArrayList<>(lst);
        sol.add(0, a);
        return sol;
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
