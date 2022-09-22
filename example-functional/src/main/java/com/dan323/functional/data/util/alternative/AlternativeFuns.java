package com.dan323.functional.data.util.alternative;

import com.dan323.functional.annotation.compiler.util.AlternativeUtil;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.data.list.List;

import java.util.function.Function;

public final class AlternativeFuns {

    private AlternativeFuns() {
        throw new UnsupportedOperationException();
    }

    public static <F> F some(IAlternative<? extends F> alternative, F felement) {
        return ApplicativeUtil.fapply(alternative, many(alternative, felement), FunctorUtil.map(alternative, felement, x -> (Function<List, List>) ((List lst) -> List.cons(x, lst))));
    }

    public static <F> F many(IAlternative<? extends F> alternative, F felement) {
        return AlternativeUtil.disj(alternative, some(alternative, felement), ApplicativeUtil.pure(alternative, List.nil()));
    }
}
