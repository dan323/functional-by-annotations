package com.dan323.functional.annotation.funcs;

public interface IApplicative<F> extends IFunctor<F> {
    String PURE = "pure";
    String FAPPLY = "fapply";
    String LIFT_A2 = "liftA2";

}
