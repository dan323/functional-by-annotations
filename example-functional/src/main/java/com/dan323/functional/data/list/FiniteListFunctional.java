package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.annotation.funcs.IMonad;
import com.dan323.functional.data.util.alternative.AlternativeMonoid;

import java.util.function.Function;

import static com.dan323.functional.data.list.ListUtils.concat;

@Monad
@Alternative
public final class FiniteListFunctional implements IMonad<FiniteList<?>>, IAlternative<FiniteList<?>> {

    private FiniteListFunctional() {
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
