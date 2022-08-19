package com.dan323.functional.annotation.funcs;

public interface IMonad<F> extends IApplicative<F> {

    String JOIN = "join";
    String FLAT_MAP = "flatMap";

}
