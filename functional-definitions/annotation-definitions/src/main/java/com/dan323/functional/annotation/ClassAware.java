package com.dan323.functional.annotation;

public interface ClassAware<F> {

    Class<F> getClassAtRuntime();

}
