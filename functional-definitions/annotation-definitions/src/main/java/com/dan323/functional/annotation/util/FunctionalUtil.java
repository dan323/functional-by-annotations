package com.dan323.functional.annotation.util;

import com.dan323.functional.annotation.*;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFoldable;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.funcs.IMonad;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public final class FunctionalUtil {

    private FunctionalUtil() {
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

    private static <G> boolean isFoldable(Class<G> gClass) {
        return gClass.getAnnotation(Foldable.class) != null;
    }

    private static <G> boolean isMonoid(Class<G> gClassz) {
        return gClassz.getAnnotation(Monoid.class) != null;
    }

    private static <G> boolean implementsStaticMethod(Class<G> gClass, String name, Class<?>... inputs) {
        try {
            gClass.getMethod(name, inputs);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    private static <G> Method getMethod(Class<G> gClass, String name, Class<?>... inputs) {
        try {
            return gClass.getMethod(name, inputs);
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

    private static <G, O> O invokeStaticMethod(G structure, Method method, Object... inputs) {
        try {
            return (O) method.invoke(structure, inputs);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("The method or inputs were not accessible", e);
        } catch (InvocationTargetException e) {
            InvocationTargetException ex = e;
            while (ex.getTargetException() instanceof IllegalArgumentException illegalArgumentException && illegalArgumentException.getCause() instanceof InvocationTargetException invocationTargetException) {
                ex = invocationTargetException;
            }
            throw new IllegalArgumentException(String.format("There was an error executing %s: %s", method.getName(), ex.getTargetException().getMessage()), ex);
        }
    }

    // FOLDABLE
    // Fold
    static <G extends IFoldable<FW>, FW extends F, FA extends F, F, A> Optional<A> foldableFold(G foldable, IMonoid<A> monoid, Class<F> fClass, FA base) {
        if (isFoldable(foldable.getClass())) {
            return getMethodIfExists(foldable.getClass(), IFoldable.FOLD_NAME, IMonoid.class, fClass)
                    .<A>map(m -> invokeStaticMethod(foldable, m, monoid, base))
                    .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLD_MAP_NAME, IMonoid.class, Function.class, fClass)
                            .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLDR_NAME, BiFunction.class, Object.class, fClass))
                            .map(m -> FoldableUtil.foldMap(foldable, monoid, fClass, Function.identity(), base)));
        } else {
            throw new IllegalArgumentException("The foldable is not correctly defined");
        }
    }

    // FoldMap
    static <G extends IFoldable<FW>, FW extends F, FA extends F, F, A, M> Optional<M> foldableFoldMap(G foldable, IMonoid<M> monoid, Class<F> fClass, Function<A, M> function, FA base) {
        if (isFoldable(foldable.getClass())) {
            return getMethodIfExists(foldable.getClass(), IFoldable.FOLD_MAP_NAME, IMonoid.class, Function.class, fClass)
                    .<M>map(m -> invokeStaticMethod(foldable, m, monoid, function, base))
                    .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLDR_NAME, BiFunction.class, Object.class, fClass)
                            .map(m -> FoldableUtil.foldr(foldable, fClass, (A x, M y) -> SemigroupUtil.op(monoid, function.apply(x), y), MonoidUtil.unit(monoid), base)));
        } else {
            throw new IllegalArgumentException("The foldable is not correctly defined");
        }
    }

    // Foldr
    static <G extends IFoldable<FW>, FW extends F, M, B, F, FM extends F> Optional<B> foldableFoldr(G foldable, Class<F> fClass, BiFunction<M, B, B> function, B b, FM fm) {
        if (isFoldable(foldable.getClass())) {
            return getMethodIfExists(foldable.getClass(), IFoldable.FOLDR_NAME, BiFunction.class, Object.class, fClass)
                    .<B>map(m -> invokeStaticMethod(foldable, m, function, b, fm))
                    .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLD_MAP_NAME, IMonoid.class, Function.class, fClass)
                            .map(m -> FoldableUtil.foldMap(foldable, new IMonoid<Function<B, B>>() {
                                public Function<B, B> op(Function<B, B> f1, Function<B, B> f2) {
                                    return f1.compose(f2);
                                }

                                public Function<B, B> unit() {
                                    return Function.identity();
                                }
                            }, fClass, (Function<M, Function<B, B>>) (M x) -> ((B y) -> function.apply(x, y)), fm).apply(b)));
        } else {
            throw new IllegalArgumentException("The foldable is not correctly defined");
        }
    }

    // SEMIGROUP
    // Operation
    static <G extends ISemigroup<A>, A> Optional<A> semigroupOp(G semigroup, A a, A b) {
        if (isSemigroup(semigroup.getClass())) {
            return Stream.of(semigroup.getClass().getGenericInterfaces())
                    .filter(iface -> iface.getTypeName().contains("IMonoid") || iface.getTypeName().contains("ISemigroup"))
                    .findFirst()
                    .map(iface -> (Class<?>) ((ParameterizedType) iface).getActualTypeArguments()[0])
                    .flatMap(aClass -> getMethodIfExists(semigroup.getClass(), ISemigroup.OP_NAME, aClass, aClass))
                    .or(() -> getMethodIfExists(semigroup.getClass(), ISemigroup.OP_NAME, Object.class, Object.class))
                    .map(m -> invokeStaticMethod(semigroup, m, a, b));
        } else {
            throw new IllegalArgumentException("The semigroup is not correctly defined");
        }
    }

    // MONOID
    // Unit function
    static <G extends IMonoid<A>, A> Optional<A> monoidUnit(G monoid) {
        if (isMonoid(monoid.getClass())) {
            return getMethodIfExists(monoid.getClass(), IMonoid.UNIT_NAME)
                    .map(m -> invokeStaticMethod(monoid, m));
        } else {
            throw new IllegalArgumentException("The monoid is not correctly defined");
        }
    }

    // FUNCTOR
    // Map functions
    static <G extends IFunctor<FW>, FW extends F, F, FA extends F, FB extends F, A, B> Optional<FB> applicativeMap(G functor, Class<F> fClass, FA base, Function<A, B> map) {
        if (isApplicative(functor.getClass())) {
            IApplicative<FW> applicative = (IApplicative<FW>) functor;
            return getMethodIfExists(applicative.getClass(), "fapply", fClass, fClass)
                    .or(() -> getMethodIfExists(applicative.getClass(), "liftA2", BiFunction.class, fClass, fClass))
                    .map(m -> ApplicativeUtil.fapply(applicative, fClass, base, ApplicativeUtil.pure(applicative, fClass, map)));
        } else {
            throw new IllegalArgumentException("The functor is not correctly defined");
        }
    }

    static <G extends IFunctor<FW>, FW extends F, F, FA extends F, FB extends F, A, B> Optional<FB> monadMap(G functor, Class<F> fClass, FA base, Function<A, B> map) {
        if (isMonad(functor.getClass())) {
            IMonad<FW> monad = (IMonad<FW>) functor;
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.PURE_NAME, Object.class))
                    .map(m -> MonadUtil.flatMap(monad, fClass, (A a) -> ApplicativeUtil.pure(monad, fClass, map.apply(a)), base));
        } else {
            throw new IllegalArgumentException("The applicative is not correctly defined");
        }
    }

    static <G extends IFunctor<FW>, FW extends F, F, FA extends F, FB extends F, A, B> Optional<FB> functorMap(G functor, Class<F> fClass, FA base, Function<A, B> map) {
        if (isFunctor(functor.getClass())) {
            return getMethodIfExists(functor.getClass(), IFunctor.MAP_NAME, fClass, Function.class)
                    .map(m -> FunctionalUtil.invokeStaticMethod(functor, m, base, map));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // APPLICATIVE
    // Pure functions
    static <G extends IApplicative<FW>, FW extends F, F, FA extends F, A> Optional<FA> applicativePure(G applicative, Class<F> fClass, A a) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.PURE_NAME, Object.class)
                    .map(m -> invokeStaticMethod(applicative, m, a));
        } else {
            throw new IllegalArgumentException("The applicative is not correctly defined.");
        }
    }

    // Fapply functions

    static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F, FF extends F> Optional<FB> applicativeFapply(G applicative, Class<F> fClass, FA base, FF ff) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.FAPPLY_NAME, fClass, fClass)
                    .<FB>map(m -> invokeStaticMethod(applicative, m, ff, base))
                    .or(() -> getMethodIfExists(applicative.getClass(), IApplicative.LIFT_A2_NAME, BiFunction.class, fClass, fClass)
                            .map(m -> ApplicativeUtil.liftA2(applicative, fClass, (Function<Object, Object> a, Object b) -> a.apply(b), ff, base)));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F, FF extends F> Optional<FB> monadFapply(G applicative, Class<F> fClass, FA base, FF ff) {
        if (isMonad(applicative.getClass())) {
            IMonad<FW> monad = (IMonad<FW>) applicative;
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, fClass)
                            .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.MAP_NAME, fClass, Function.class)))
                    .map(m -> MonadUtil.flatMap(monad, fClass, f -> FunctorUtil.map(monad, fClass, base, (Function<?, ?>) f), ff));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // LiftA2 functions

    static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F, FC extends F, A, B, C> Optional<FC> applicativeLiftA2(G applicative, Class<F> fClass, BiFunction<A, B, C> mapping, FA fa, FB fb) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.LIFT_A2_NAME, BiFunction.class, fClass, fClass)
                    .<FC>map(m -> invokeStaticMethod(applicative, m, mapping, fa, fb))
                    .or(() -> getMethodIfExists(applicative.getClass(), IApplicative.FAPPLY_NAME, fClass, fClass)
                            .map(m -> ApplicativeUtil.fapply(applicative, fClass, fb, FunctorUtil.map(applicative, fClass, fa, (A a) -> (Function<B, C>) (b -> mapping.apply(a, b))))));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    static <G extends IApplicative<FW>, FW extends F, F, FA extends F, FB extends F, FC extends F, A, B, C> Optional<FC> monadLiftA2(G applicative, Class<F> fClass, BiFunction<A, B, C> mapping, FA fa, FB fb) {
        if (isMonad(applicative.getClass())) {
            IMonad<FW> monad = (IMonad<FW>) applicative;
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, fClass)
                            .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.MAP_NAME, fClass, Function.class)))
                    .map(m -> ApplicativeUtil.fapply(monad, fClass, fb, FunctorUtil.map(monad, fClass, fa, (A a) -> (Function<B, C>) (b -> mapping.apply(a, b)))));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // MONAD
    // FlatMap functions

    static <G extends IMonad<FW>, FW extends F, F, FA extends F, FB extends F, A> Optional<FB> monadFlatMap(G monad, Class<F> fClass, Function<A, FB> mapping, FA fa) {
        if (isMonad(monad.getClass())) {
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, fClass)
                    .<FB>map(m -> invokeStaticMethod(monad, m, mapping, fa))
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, fClass)
                            .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.MAP_NAME, fClass, Function.class))
                            .map(m -> MonadUtil.join(monad, fClass, FunctorUtil.map(monad, fClass, fa, mapping))));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

    // Join functions

    static <G extends IMonad<FW>, FW extends F, F, FFA extends F, FA extends F> Optional<FA> monadJoin(G monad, Class<F> fClass, FFA ffa) {
        if (isMonad(monad.getClass())) {
            return getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, fClass)
                    .<FA>map(m -> invokeStaticMethod(monad, m, ffa))
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, fClass)
                            .map(m -> MonadUtil.flatMap(monad, fClass, Function.identity(), ffa)));
        } else {
            throw new IllegalArgumentException("The class is not annotated");
        }
    }

}