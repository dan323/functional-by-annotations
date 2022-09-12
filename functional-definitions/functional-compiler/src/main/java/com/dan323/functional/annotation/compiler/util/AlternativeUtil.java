package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.funcs.IAlternative;

public final class AlternativeUtil {

    private AlternativeUtil() {
        throw new UnsupportedOperationException();
    }

    public static <F> F empty(IAlternative<? extends F> alternative) {
        return FunctionalUtil.<F>alternativeEmpty(alternative)
                .orElseThrow(() -> new IllegalArgumentException("The alternative is not correctly implemented."));
    }

    public static <F> F disj(IAlternative<? extends F> alternative, F fa, F fb) {
        return FunctionalUtil.alternativeDisj(alternative, fa, fb)
                .orElseThrow(() -> new IllegalArgumentException("The alternative is not correctly implemented."));
    }
}
