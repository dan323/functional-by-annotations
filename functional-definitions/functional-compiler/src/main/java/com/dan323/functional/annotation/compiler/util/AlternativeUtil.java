package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.funcs.IAlternative;

public final class AlternativeUtil {

    public static final String NOT_CORRECTLY_IMPLEMENTED = "The alternative is not correctly implemented.";

    private AlternativeUtil() {
        throw new UnsupportedOperationException();
    }

    public static <F> F empty(IAlternative<? extends F> alternative) {
        return FunctionalUtil.<F>alternativeEmpty(alternative)
                .orElseThrow(() -> new IllegalArgumentException(NOT_CORRECTLY_IMPLEMENTED));
    }

    public static <F> F disjunction(IAlternative<? extends F> alternative, F fa, F fb) {
        return FunctionalUtil.alternativeDisj(alternative, fa, fb)
                .orElseThrow(() -> new IllegalArgumentException(NOT_CORRECTLY_IMPLEMENTED));
    }
}
