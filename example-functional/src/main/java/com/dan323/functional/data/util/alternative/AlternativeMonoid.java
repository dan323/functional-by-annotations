package com.dan323.functional.data.util.alternative;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.compiler.util.AlternativeUtil;
import com.dan323.functional.annotation.funcs.IAlternative;

@Monoid
public final class AlternativeMonoid<FA extends F, F> implements IMonoid<FA> {

    private final IAlternative<F> alternative;

    public AlternativeMonoid(IAlternative<F> alternative) {
        this.alternative = alternative;
    }

    public FA op(FA first, FA second) {
        return (FA) AlternativeUtil.disj(alternative, first, second);
    }

    public FA unit() {
        return (FA) AlternativeUtil.empty(alternative);
    }
}
