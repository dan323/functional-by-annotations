package com.dan323.functional.data.util.foldable;

import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFoldable;
import com.dan323.functional.data.bool.AndMonoid;
import com.dan323.functional.data.bool.OrMonoid;
import com.dan323.functional.data.integer.SumMonoid;

import java.util.Objects;
import java.util.function.Predicate;

import static com.dan323.functional.annotation.compiler.util.FoldableUtil.foldMap;

public final class FoldableFuns {

    private FoldableFuns() {
        throw new UnsupportedOperationException();
    }

    /**
     * Test if the foldable is empty
     *
     * @param foldable implementation of {@link IFoldable<F>}
     * @param felement element of type {@code fClass} appplied to some type {@code A}
     * @param <F>      Type of {@code fClass}
     * @return true iff {@code felement} has no elements to be folded
     */
    public static <F> boolean isEmpty(IFoldable<? extends F> foldable, F felement) {
        return foldMap(foldable, AndMonoid.AND_MONOID, x -> Boolean.FALSE, felement);
    }

    /**
     * Size of the foldable element
     *
     * @param foldable implementation of {@link IFoldable<F>}
     * @param felement element of type {@code fClass} appplied to some type {@code A}
     * @param <F>      Type of {@code fClass}
     * @return number of elements in {@code felement}
     */
    public static <F> int length(IFoldable<? extends F> foldable, F felement) {
        return foldMap(foldable, SumMonoid.getInstance(), x -> 1, felement);
    }

    /**
     * Test if the foldable element contains a specific element
     *
     * @param foldable implementation of {@link IFoldable<F>}
     * @param felement element of type {@code fClass} appplied to some type {@code A}
     * @param elem     elemnt to look for
     * @param <F>      Type of {@code fClass}
     * @param <A>      Type of {@code elem}
     * @return true iff {@code elem} is in {@code felement}
     */
    public static <F, A> boolean contains(IFoldable<? extends F> foldable, A elem, F felement) {
        return foldMap(foldable, OrMonoid.OR_MONOID, x -> Objects.equals(x, elem), felement);
    }

    /**
     * Keep elements that satisfy the predicate
     *
     * @param foldable foldable implementation for F
     * @param applicative applicative implementation for F
     * @param monoid monoid implementation F
     * @param fold container of F type
     * @param predicate Predicate on contained elements
     * @return a container of type F whose elements are in fold, and satisfy the predicate
     * @param <F> type of container
     * @param <A> type of the contained elements
     */
    public static <F, A> F filter(IFoldable<? extends F> foldable, IApplicative<? extends F> applicative, IMonoid<? extends F> monoid, F fold, Predicate<A> predicate) {
        return foldMap(foldable, monoid, (A x) -> predicate.test(x) ? ApplicativeUtil.pure(applicative, x) : MonoidUtil.unit(monoid), fold);
    }
}
