package com.dan323.functional.foldable;

import com.dan323.functional.annotation.util.FoldableUtil;
import com.dan323.functional.monoid.MonoidSum;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FoldableUtilTest {
    @Test
    public void foldableFoldFomFoldMap() {
        var sol = FoldableUtil.fold(ListFoldFoldMap.LIST_FOLD, MonoidSum.MONOID, List.class, List.of(3, 4, 5));
        assertEquals(12, sol);
    }

    @Test
    public void foldableFoldFromFoldr() {
        var sol = FoldableUtil.fold(ListFoldFoldr.LIST_FOLD, MonoidSum.MONOID, List.class, List.of(3, 4, 5));
        assertEquals(12, sol);
    }

    @Test
    public void foldableFold() {
        var sol = FoldableUtil.fold(FoldableWithFold.LIST_FOLD, MonoidSum.MONOID, List.class, List.of(3, 5, 5));
        assertEquals(13, sol);
    }

    @Test
    public void foldableFoldrRun() {
        var sol = FoldableUtil.foldr(ListFoldFoldr.LIST_FOLD, List.class, this::apply, 1, List.of(true, false, true, false));
        assertEquals(7, sol);
    }

    public int apply(boolean b, int x) {
        if (b) {
            return x + 1;
        } else {
            return x * 2;
        }
    }

    @Test
    public void foldableNoAnnotationError() {
        assertThrows(IllegalArgumentException.class, () -> FoldableUtil.fold(new FoldableNoAnnotation(), MonoidSum.MONOID, List.class, List.of(3, 4, 5)));
    }

    @Test
    public void semigroupNoMethodError() {
        assertThrows(IllegalArgumentException.class, () -> FoldableUtil.fold(new EmptyFoldable(), MonoidSum.MONOID, List.class, List.of(3, 4, 5)));
    }
}
