package com.dan323.functional.util.traversal;

import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.ITraversal;
import com.dan323.functional.util.foldable.ListFoldFoldr;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Traversal
public class TraversalList implements ITraversal<List<?>> {

    public <K, A> K traverse(IApplicative<K> applicative, Function<A, K> fun, List<A> lst) {
        var empty = ApplicativeUtil.pure(applicative, List.of());
        ListFoldFoldr listFoldFoldr = new ListFoldFoldr();
        return listFoldFoldr.foldr((x, y) -> ApplicativeUtil.liftA2(applicative, (BiFunction<A, List<A>, List<A>>) this::cons, fun.apply(x), y), empty, lst);
    }

    private boolean isApplicative(Type type) {
        return type.getTypeName().contains("IApplicative") || type.getTypeName().contains("IMonad");
    }

    private <A> List<A> cons(A a, List<A> lst) {
        var sol = new ArrayList<>(lst);
        sol.add(0, a);
        return sol;
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
