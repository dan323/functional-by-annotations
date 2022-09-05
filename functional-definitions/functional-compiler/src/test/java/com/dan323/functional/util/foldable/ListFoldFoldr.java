package com.dan323.functional.util.foldable;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.funcs.IFoldable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Foldable
public class ListFoldFoldr implements IFoldable<List<?>> {

    public <A,B> B foldr(BiFunction<A,B,B> function, B b, List<A> lst){
        return lst.stream().<Function<B,B>>map(x -> (y -> function.apply(x,y))).reduce(Function.identity(), Function::compose).apply(b);
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }

    public static final ListFoldFoldr LIST_FOLD = new ListFoldFoldr();
}
