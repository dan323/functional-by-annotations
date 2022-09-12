package com.dan323.mock;

import com.dan323.functional.annotation.Foldable;
import com.dan323.functional.annotation.funcs.IFoldable;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@Foldable
public class SomeFoldable implements IFoldable<List<?>> {

    public <A, B> B foldr(BiFunction<A, B, B> function, B b, List<A> fa) {
        return fa.stream()
                .map(x -> (Function<B, B>) ((B y) -> function.apply(x, y)))
                .reduce(Function.identity(), Function::compose)
                .apply(b);
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class) List.class;
    }
}
