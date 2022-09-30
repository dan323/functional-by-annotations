package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.Traversal;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.annotation.funcs.ITraversal;
import com.dan323.functional.data.util.alternative.AlternativeMonoid;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.dan323.functional.data.list.ListUtils.concat;

@Monad
@Traversal
@Alternative
public final class FiniteListFunctional implements IMonad<FiniteList<?>>, IAlternative<FiniteList<?>>, ITraversal<FiniteList<?>> {

    private FiniteListFunctional() {
    }

    public static <K, A> K traverse(IApplicative<K> applicative, Function<A, K> fun, FiniteList<A> lst) {
        var empty = ApplicativeUtil.pure(applicative, List.of());
        return foldr((x, y) -> ApplicativeUtil.liftA2(applicative, (BiFunction<A, FiniteList<A>, FiniteList<A>>) FiniteList::cons, fun.apply(x), y), empty, lst);
    }

    public static <A, B> B foldr(BiFunction<A, B, B> function, B b, FiniteList<A> lst) {
        return lst.head().maybe(h -> foldr(function, function.apply(h, b), lst.tail()), b);
    }

    private static final FiniteListFunctional FINITE_LIST_FUNCTIONAL = new FiniteListFunctional();

    public static FiniteListFunctional getInstance() {
        return FINITE_LIST_FUNCTIONAL;
    }

    public static <A> AlternativeMonoid<FiniteList<A>, FiniteList<?>> getAlternativeMonoid() {
        return new AlternativeMonoid<>(getInstance());
    }

    public static <A, B> FiniteList<B> map(FiniteList<A> finiteList, Function<A, B> mapping) {
        return finiteList.head().maybe(h -> FiniteList.cons(mapping.apply(h), map(finiteList.tail(), mapping)), FiniteList.nil());
    }

    public static <A> FiniteList<A> disjunction(FiniteList<A> op1, FiniteList<A> op2) {
        return ListUtils.concat(op1, op2);
    }

    public static <A> FiniteList<A> empty() {
        return FiniteList.nil();
    }

    public static <A> FiniteList<A> pure(A a) {
        return FiniteList.of(a);
    }

    public static <A, B> FiniteList<B> flatMap(Function<A, FiniteList<B>> f, FiniteList<A> base) {
        return base.head().maybe(h -> concat(f.apply(h), flatMap(f, base.tail())), FiniteList.nil());
    }

    @Override
    public Class<FiniteList<?>> getClassAtRuntime() {
        return (Class<FiniteList<?>>) (Class) FiniteList.class;
    }
}
