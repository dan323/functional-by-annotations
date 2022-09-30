package com.dan323.functional.data.writer;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;

@Monad
public final class Accumulator<A> extends WriterMonad<FiniteList<A>> {

    public Accumulator() {
        super(FiniteListFunctional.getAlternativeMonoid());
    }
}
