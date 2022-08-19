package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.*;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.funcs.IMonad;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class FunctionalUtils {

    private FunctionalUtils() {
        throw new UnsupportedOperationException();
    }

    private static <G> boolean isApplicative(Class<G> gClass) {
        return gClass.getAnnotation(Applicative.class) != null || gClass.getAnnotation(Monad.class) != null;
    }

    private static <G> boolean isMonad(Class<G> gClass) {
        return gClass.getAnnotation(Monad.class) != null;
    }

    private static <G> boolean isFunctor(Class<G> gClass) {
        return gClass.getAnnotation(Functor.class) != null || gClass.getAnnotation(Applicative.class) != null || gClass.getAnnotation(Monad.class) != null;
    }

    private static <G> boolean isSemigroup(Class<G> gClassz) {
        return gClassz.getAnnotation(Semigroup.class) != null || gClassz.getAnnotation(Monoid.class) != null;
    }

    private static <G> boolean isMonoid(Class<G> gClassz) {
        return gClassz.getAnnotation(Monoid.class) != null;
    }

    private static <G> boolean implementsStaticMethod(Class<G> gClass, String name, Class<?>... inputs) {
        try {
            gClass.getDeclaredMethod(name, inputs);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static <G> Method getMethod(Class<G> gClass, String name, Class<?>... inputs) {
        try {
            return gClass.getDeclaredMethod(name, inputs);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static <G> Optional<Method> getMethodIfExists(Class<G> gClass, String name, Class<?>... inputs) {
        if (implementsStaticMethod(gClass, name, inputs)) {
            return Optional.of(getMethod(gClass, name, inputs));
        } else {
            return Optional.empty();
        }
    }

    private static <O> O invokeStaticMethod(Method method, Object... inputs) {
        try {
            return (O) method.invoke(null, inputs);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("The method or inputs were not accessible");
        } catch (InvocationTargetException e) {
            InvocationTargetException ex = e;
            while (ex.getTargetException() instanceof IllegalArgumentException illegalArgumentException && illegalArgumentException.getCause() instanceof InvocationTargetException invocationTargetException) {
                ex = invocationTargetException;
            }
            throw new IllegalArgumentException(String.format("There was an error executing %s: %s", method.getName(), ex.getTargetException().getMessage()), ex);
        }
    }

    // SEMIGROUP
    // Operation
    static <G, A> Optional<A> semigroupOp(Class<G> gClass, Class<A> aClass, A a, A b) {
        if (isSemigroup(gClass)) {
            return getMethodIfExists(gClass, ISemigroup.OP_NAME, aClass, aClass)
                    .map(m -> invokeStaticMethod(m, a, b));
        } else {
            throw new IllegalArgumentException("The semigroup is not correctly defined");
        }
    }

    static <G, A> Optional<A> monoidOp(Class<G> gClass, Class<A> aClass, A a, A b) {
        if (isMonoid(gClass)) {
            return getMethodIfExists(gClass, IMonoid.OP_NAME, aClass, aClass)
                    .map(m -> invokeStaticMethod(m, a, b));
        } else {
            throw new IllegalArgumentException("The monoid is not correctly defined");
        }
    }

    // MONOID
    // Unit function
    static <G, A> Optional<A> monoidUnit(Class<G> gClass, Class<A> aClass) {
        if (isMonoid(gClass)) {
            return getMethodIfExists(gClass, IMonoid.UNIT_NAME)
                    .map(FunctionalUtils::invokeStaticMethod);
        } else {
            throw new IllegalArgumentException("The monoid is not correctly defined");
        }
    }

    // FUNCTOR
    // Map functions
    static <G, F, FA extends F, FB extends F, A, B> Optional<FB> applicativeMap(Class<G> gClass, Class<F> fClass, FA base, Function<A, B> map) {
        if (isApplicative(gClass)) {
            return getMethodIfExists(gClass, "fapply", fClass, fClass)
                    .or(() -> getMethodIfExists(gClass, "liftA2", BiFunction.class, fClass, fClass))
                    .map(m -> ApplicativeUtil.fapply(gClass, fClass, base, ApplicativeUtil.pure(gClass, fClass, map)));
        } else {
            throw new IllegalArgumentException("The functor is not correctly defined");
        }
    }

    static <G, F, FA extends F, FB extends F, A, B> Optional<FB> monadMap(Class<G> gClass, Class<F> fClass, FA base, Function<A, B> map) {
        if (isMonad(gClass)) {
            return getMethodIfExists(gClass, IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .flatMap(m -> getMethodIfExists(gClass, IMonad.PURE_NAME, Object.class))
                    .map(m -> MonadUtil.flatMap(gClass, fClass, (A a) -> ApplicativeUtil.pure(gClass, fClass, map.apply(a)), base));
        } else {
            throw new IllegalArgumentException("The applicative is not correctly defined");
        }
    }

    static <G, F, FA extends F, FB extends F, A, B> Optional<FB> functorMap(Class<G> gClass, Class<F> fClass, FA base, Function<A, B> map) {
        if (isFunctor(gClass)) {
            return getMethodIfExists(gClass, IFunctor.MAP_NAME, fClass, Function.class)
                    .map(m -> FunctionalUtils.invokeStaticMethod(m, base, map));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // APPLICATIVE
    // Pure functions
    static <G, F, FA extends F, A> Optional<FA> applicativePure(Class<G> gClass, Class<F> fClass, A a) {
        if (isApplicative(gClass)) {
            return getMethodIfExists(gClass, IApplicative.PURE_NAME, Object.class)
                    .map(m -> invokeStaticMethod(m, a));
        } else {
            throw new IllegalArgumentException("The applicative is not correctly defined.");
        }
    }

    // Fapply functions

    static <G, F, FA extends F, FB extends F, FF extends F> Optional<FB> applicativeFapply(Class<G> gClass, Class<F> fClass, FA base, FF ff) {
        if (isApplicative(gClass)) {
            return getMethodIfExists(gClass, IApplicative.FAPPLY_NAME, fClass, fClass)
                    .<FB>map(m -> invokeStaticMethod(m, ff, base))
                    .or(() -> getMethodIfExists(gClass, IApplicative.LIFT_A2_NAME, BiFunction.class, fClass, fClass)
                            .map(m -> ApplicativeUtil.liftA2(gClass, fClass, (Function<Object, Object> a, Object b) -> a.apply(b), ff, base)));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    static <G, F, FA extends F, FB extends F, FF extends F> Optional<FB> monadFapply(Class<G> gClass, Class<F> fClass, FA base, FF ff) {
        if (isMonad(gClass)) {
            return getMethodIfExists(gClass, IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .or(() -> getMethodIfExists(gClass, IMonad.JOIN_NAME, fClass)
                            .flatMap(m -> getMethodIfExists(gClass, IMonad.MAP_NAME, fClass, Function.class)))
                    .map(m -> MonadUtil.flatMap(gClass, fClass, f -> FunctorUtil.map(gClass, fClass, base, (Function<?, ?>) f), ff));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // LiftA2 functions

    static <G, F, FA extends F, FB extends F, FC extends F, A, B, C> Optional<FC> applicativeLiftA2(Class<G> gClass, Class<F> fClass, BiFunction<A, B, C> mapping, FA fa, FB fb) {
        if (isApplicative(gClass)) {
            return getMethodIfExists(gClass, IApplicative.LIFT_A2_NAME, BiFunction.class, fClass, fClass)
                    .<FC>map(m -> invokeStaticMethod(m, mapping, fa, fb))
                    .or(() -> getMethodIfExists(gClass, IApplicative.FAPPLY_NAME, fClass, fClass)
                            .map(m -> ApplicativeUtil.fapply(gClass, fClass, fb, FunctorUtil.map(gClass, fClass, fa, (A a) -> (Function<B, C>) (b -> mapping.apply(a, b))))));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    static <G, F, FA extends F, FB extends F, FC extends F, A, B, C> Optional<FC> monadLiftA2(Class<G> gClass, Class<F> fClass, BiFunction<A, B, C> mapping, FA fa, FB fb) {
        if (isMonad(gClass)) {
            return getMethodIfExists(gClass, IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .or(() -> getMethodIfExists(gClass, IMonad.JOIN_NAME, fClass)
                            .flatMap(m -> getMethodIfExists(gClass, IMonad.MAP_NAME, fClass, Function.class)))
                    .map(m -> ApplicativeUtil.fapply(gClass, fClass, fb, FunctorUtil.map(gClass, fClass, fa, (A a) -> (Function<B, C>) (b -> mapping.apply(a, b)))));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // MONAD
    // FlatMap functions

    static <G, F, FA extends F, FB extends F, A> Optional<FB> monadFlatMap(Class<G> gClass, Class<F> fClass, Function<A, FB> mapping, FA fa) {
        if (isMonad(gClass)) {
            return getMethodIfExists(gClass, IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .<FB>map(m -> invokeStaticMethod(m, mapping, fa))
                    .or(() -> getMethodIfExists(gClass, IMonad.JOIN_NAME, fClass)
                            .flatMap(m -> getMethodIfExists(gClass, IMonad.MAP_NAME, fClass, Function.class))
                            .map(m -> MonadUtil.join(gClass, fClass, FunctorUtil.map(gClass, fClass, fa, mapping))));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // Join functions

    static <G, F, FFA extends F, FA extends F> Optional<FA> monadJoin(Class<G> gClass, Class<F> fClass, FFA ffa) {
        if (isMonad(gClass)) {
            return getMethodIfExists(gClass, IMonad.JOIN_NAME, fClass)
                    .<FA>map(m -> invokeStaticMethod(m, ffa))
                    .or(() -> getMethodIfExists(gClass, IMonad.FLAT_MAP_NAME, Function.class, fClass)
                            .map(m -> MonadUtil.flatMap(gClass, fClass, Function.identity(), ffa)));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

}