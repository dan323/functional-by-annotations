package com.dan323.functional.data.pair;

import com.dan323.functional.annotation.funcs.Functional;
import com.dan323.functional.annotation.funcs.IFunctor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

public abstract class ProdFunctional<M,N> implements Functional {
    protected final Functional mFunctor;
    protected final Functional nFunctor;
    protected final Class<M> mClass;
    protected final Class<N> nClass;

    public ProdFunctional(Functional fm, Functional fn) {
        this.mFunctor = fm;
        this.nFunctor = fn;
        this.nClass = extractClass(fn);
        this.mClass = extractClass(fm);
    }

    private <U> Class<U> extractClass(Functional fu) {
        return Stream.of(fu.getClass()
                        .getGenericInterfaces()).filter(this::isRightFunctional)
                .findFirst()
                .map(u -> {
                    try {
                        return (Class<U>) Class.forName(((ParameterizedType) ((ParameterizedType) u).getActualTypeArguments()[0]).getRawType().getTypeName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).orElseThrow();
    }

    public abstract boolean isRightFunctional(Type type);
}
