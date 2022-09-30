package com.dan323.functional.data.util.alternative;

import com.dan323.functional.annotation.compiler.util.AlternativeUtil;
import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Implementation of {@link #many(IAlternative, IAlternative, Object, Function)} and {@link #some(IAlternative, IAlternative, Object, Function)}
 * to be executed lazily.
 * <br>
 * NOTE: It is recommended to reimplement, as once the specific classes are identified it is easier to execute lazily.
 */
public final class AlternativeFuns {

    private AlternativeFuns() {
        throw new UnsupportedOperationException();
    }

    public static <F> Supplier<F> some(IAlternative<Supplier<F>> alternative, IAlternative<F> alternativef, F felement) {
        return () -> (ApplicativeUtil.fapply(alternative, many(alternative, alternativef, felement), () -> FunctorUtil.map(alternativef, felement, x -> (Function<FiniteList, FiniteList>) ((FiniteList lst) -> FiniteList.cons(x, lst))))).get();
    }

    public static <F> Supplier<F> many(IAlternative<Supplier<F>> alternative, IAlternative<F> alternativef, F felement) {
        return () -> (AlternativeUtil.disj(alternative, some(alternative, alternativef, felement), () -> ApplicativeUtil.pure(alternativef, List.nil()))).get();
    }
}
