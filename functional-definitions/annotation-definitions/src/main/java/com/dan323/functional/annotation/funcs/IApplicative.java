package com.dan323.functional.annotation.funcs;

public interface IApplicative<F> extends IFunctor<F> {
    String PURE_NAME = "pure";
    String FAPPLY_NAME = "fapply";
    String LIFT_A2_NAME = "liftA2";

}
