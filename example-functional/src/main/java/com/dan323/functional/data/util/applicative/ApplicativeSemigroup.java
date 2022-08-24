package com.dan323.functional.data.util.applicative;

import com.dan323.functional.annotation.Semigroup;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.util.ApplicativeUtil;
import com.dan323.functional.annotation.util.SemigroupUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

@Semigroup
public class ApplicativeSemigroup<FA extends F, F, A> implements ISemigroup<FA> {

    protected final IMonoid<A> monoid;
    protected final IApplicative<F> applicative;
    protected final Class<F> fClass;

    public ApplicativeSemigroup(IMonoid<A> monoid, IApplicative<F> applicative) {
        this.applicative = applicative;
        this.monoid = monoid;
        this.fClass = extractClass(applicative);
    }

    private Class<F> extractClass(IApplicative<F> fu) {
        return Stream.of(fu.getClass()
                        .getGenericInterfaces()).filter(this::isApplicative)
                .findFirst()
                .map(u -> {
                    try {
                        return (Class<F>) Class.forName(((ParameterizedType) ((ParameterizedType) u).getActualTypeArguments()[0]).getRawType().getTypeName());
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).orElseThrow();
    }

    private boolean isApplicative(Type type) {
        return type.getTypeName().contains("IApplicative") || type.getTypeName().contains("IMonad");
    }

    public FA op(FA elem1, FA elem2) {
        return ApplicativeUtil.liftA2(applicative, fClass, (x, y) -> SemigroupUtil.op(monoid, x, y), elem1, elem2);
    }
}
