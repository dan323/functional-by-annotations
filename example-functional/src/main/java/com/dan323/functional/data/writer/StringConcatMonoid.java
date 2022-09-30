package com.dan323.functional.data.writer;

import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public final class StringConcatMonoid implements IMonoid<String> {

    public static String op(String s1, String s2) {
        return s1 + s2;
    }

    public static String unit() {
        return "";
    }

    private static final StringConcatMonoid STRING_CONCAT_MONOID = new StringConcatMonoid();

    public static StringConcatMonoid getInstance() {
        return STRING_CONCAT_MONOID;
    }
}
