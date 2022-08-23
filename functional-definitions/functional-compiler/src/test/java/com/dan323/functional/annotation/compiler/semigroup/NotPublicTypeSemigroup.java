package com.dan323.functional.annotation.compiler.semigroup;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;

import java.util.ArrayList;
import java.util.List;

@Semigroup
class NotPublicTypeSemigroup<A> implements ISemigroup<List<A>> {

    public List<A> op(List<A> lst, List<A> lst2) {
        var sol = new ArrayList<>(lst);
        sol.addAll(lst2);
        return sol;
    }
}
