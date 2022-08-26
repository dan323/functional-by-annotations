package com.dan323.functional.data.util.foldable;

import com.dan323.functional.annotation.funcs.IFoldable;
import com.dan323.functional.data.bool.AndMonoid;
import com.dan323.functional.data.bool.OrMonoid;
import com.dan323.functional.data.integer.SumMonoid;

import java.util.Objects;

import static com.dan323.functional.annotation.util.FoldableUtil.foldMap;

public final class FoldableFuns {

    private FoldableFuns() {
        throw new UnsupportedOperationException();
    }

    /**
     * Test if the foldable is empty
     *
     * @param foldable implementation of {@link IFoldable<F>}
     * @param fClass   class of the type constructor that was made a foldable by {@code foldable}
     * @param felement element of type {@code fClass} appplied to some type {@code A}
     * @param <FA>     Type of {@code felement}
     * @param <F>      Type of {@code fClass}
     * @return true iff {@code felement} has no elements to be folded
     */
    public static <FA extends F, F> boolean isEmpty(IFoldable<F> foldable, Class<F> fClass, FA felement) {
        return foldMap(foldable, AndMonoid.AND_MONOID, fClass, x -> Boolean.FALSE, felement);
    }

    /**
     * Size of the foldable element
     *
     * @param foldable implementation of {@link IFoldable<F>}
     * @param fClass   class of the type constructor that was made a foldable by {@code foldable}
     * @param felement element of type {@code fClass} appplied to some type {@code A}
     * @param <FA>     Type of {@code felement}
     * @param <F>      Type of {@code fClass}
     * @return number of elements in {@code felement}
     */
    public static <FA extends F, F> int length(IFoldable<F> foldable, Class<F> fClass, FA felement) {
        return foldMap(foldable, SumMonoid.getInstance(), fClass, x -> 1, felement);
    }

    /**
     * Test if the foldable element contains a specific element
     *
     * @param foldable implementation of {@link IFoldable<F>}
     * @param fClass   class of the type constructor that was made a foldable by {@code foldable}
     * @param felement element of type {@code fClass} appplied to some type {@code A}
     * @param elem     elemnt to look for
     * @param <FA>     Type of {@code felement}
     * @param <F>      Type of {@code fClass}
     * @param <A>      Type of {@code elem}
     * @return true iff {@code elem} is in {@code felement}
     */
    public static <FA extends F, F, A> boolean contains(IFoldable<F> foldable, Class<F> fClass, A elem, FA felement) {
        return foldMap(foldable, OrMonoid.OR_MONOID, fClass, x -> Objects.equals(x, elem), felement);
    }
}
