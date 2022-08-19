package com.dan323.functional.annotation.funcs;

public interface IMonad<F> extends IApplicative<F> {

    String JOIN_NAME = "join";
    String FLAT_MAP_NAME = "flatMap";

}
