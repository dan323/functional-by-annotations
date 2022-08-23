package com.dan323.functional.foldable;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.funcs.IFoldable;

import java.util.List;

@Foldable
public class EmptyFoldable implements IFoldable<List<?>> {
}
