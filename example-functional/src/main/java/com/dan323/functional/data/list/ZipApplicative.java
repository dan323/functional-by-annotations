package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.Function;

@Applicative
public final class ZipApplicative implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a) {
        return List.repeat(a);
    }

    public static <A, B> List<B> fapply(List<Function<A, B>> funs, List<A> lst) {
        return funs.zip(Function::apply, lst);
    }

    public static <A, B> List<B> map(List<A> base, Function<A, B> map) {
        return base.map(map);
    }
}
