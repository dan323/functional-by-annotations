package com.dan323.functional.annotation.compiler.alternative;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.funcs.IAlternative;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Alternative
public class AlternativeMock implements IAlternative<List<?>> {

    public <A> List<A> disjunction(List<A> lst1, List<A> lst2) {
        List<A> lst = new ArrayList<>();
        lst.addAll(lst1);
        lst.addAll(lst2);
        return lst;
    }

    public <A, B> List<B> fapply(Function<A, List<B>> fun, List<A> lst) {
        return lst.stream().flatMap(x -> fun.apply(x).stream()).toList();
    }

    public <A> List<A> pure(A a){
        return List.of(a);
    }

    public <A> List<A> empty() {
        return List.of();
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
