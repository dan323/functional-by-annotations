package com.dan323.functional.util.foldable;

import com.dan323.functional.annotation.funcs.IFoldable;

import java.util.List;

public class FoldableNoAnnotation implements IFoldable<List<?>> {

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
