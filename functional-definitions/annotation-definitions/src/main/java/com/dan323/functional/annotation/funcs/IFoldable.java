package com.dan323.functional.annotation.funcs;

public interface IFoldable<F> extends Functional<F> {
    String FOLD_NAME = "fold";
    String FOLD_MAP_NAME = "foldMap";
    String FOLDR_NAME = "foldr";
}
