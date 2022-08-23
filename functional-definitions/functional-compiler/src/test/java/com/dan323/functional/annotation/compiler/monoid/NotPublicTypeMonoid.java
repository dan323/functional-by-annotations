package com.dan323.functional.annotation.compiler.monoid;

import com.dan323.functional.annotation.algs.IMonoid;

import java.util.ArrayList;
import java.util.List;

class NotPublicTypeMonoid<A> implements IMonoid<List<A>> {

    public List<A> op(List<A> lst, List<A> lst2) {
        var sol = new ArrayList<>(lst);
        sol.addAll(lst2);
        return sol;
    }
}
