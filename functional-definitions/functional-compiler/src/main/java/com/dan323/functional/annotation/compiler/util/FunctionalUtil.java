package com.dan323.functional.annotation.compiler.util;

import com.dan323.functional.annotation.*;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

final class FunctionalUtil {
    private FunctionalUtil() {
        throw new UnsupportedOperationException();
    }

    private static <G> boolean isTraversal(Class<G> gClass) {
        return gClass.getAnnotation(Traversal.class) != null;
    }

    private static <G> boolean isAlternative(Class<G> gClass) {
        return gClass.getAnnotation(Alternative.class) != null;
    }

    private static <G> boolean isApplicative(Class<G> gClass) {
        return gClass.getAnnotation(Applicative.class) != null || isMonad(gClass) || isAlternative(gClass);
    }

    private static <G> boolean isMonad(Class<G> gClass) {
        return gClass.getAnnotation(Monad.class) != null;
    }

    private static <G> boolean isFunctor(Class<G> gClass) {
        return gClass.getAnnotation(Functor.class) != null || isApplicative(gClass) || gClass.getAnnotation(Traversal.class) != null;
    }

    private static <G> boolean isSemigroup(Class<G> gClassz) {
        return gClassz.getAnnotation(Semigroup.class) != null || isMonoid(gClassz);
    }

    private static <G> boolean isFoldable(Class<G> gClass) {
        return gClass.getAnnotation(Foldable.class) != null || isTraversal(gClass);
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
            throw new IllegalStateException(String.format("The method %s or inputs were not accessible", method.getName()), e);
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
    static <F, A> Optional<A> foldableFold(IFoldable<? extends F> foldable, IMonoid<? extends A> monoid, F base) {
        if (isFoldable(foldable.getClass())) {
            return getMethodIfExists(foldable.getClass(), IFoldable.FOLD_NAME, IMonoid.class, foldable.getClassAtRuntime())
                    .<A>map(m -> invokeStaticMethod(foldable, m, monoid, base))
                    .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLD_MAP_NAME, IMonoid.class, Function.class, foldable.getClassAtRuntime())
                            .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLDR_NAME, BiFunction.class, Object.class, foldable.getClassAtRuntime()))
                            .map(m -> FoldableUtil.foldMap(foldable, monoid, Function.identity(), base)));
        } else {
            return Optional.empty();
        }
    }

    // FoldMap
    static <F, A, M> Optional<M> foldableFoldMap(IFoldable<? extends F> foldable, IMonoid<? extends M> monoid, Function<A, M> function, F base) {
        if (isFoldable(foldable.getClass())) {
            return getMethodIfExists(foldable.getClass(), IFoldable.FOLD_MAP_NAME, IMonoid.class, Function.class, foldable.getClassAtRuntime())
                    .<M>map(m -> invokeStaticMethod(foldable, m, monoid, function, base))
                    .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLDR_NAME, BiFunction.class, Object.class, foldable.getClassAtRuntime())
                            .map(m -> FoldableUtil.foldr(foldable, (A x, M y) -> SemigroupUtil.op(monoid, function.apply(x), y), MonoidUtil.unit(monoid), base)));
        } else {
            return Optional.empty();
        }
    }

    // Foldr
    static <F, M, B> Optional<B> foldableFoldr(IFoldable<? extends F> foldable, BiFunction<M, B, B> function, B b, F fm) {
        if (isFoldable(foldable.getClass())) {
            return getMethodIfExists(foldable.getClass(), IFoldable.FOLDR_NAME, BiFunction.class, Object.class, foldable.getClassAtRuntime())
                    .<B>map(m -> invokeStaticMethod(foldable, m, function, b, fm))
                    .or(() -> getMethodIfExists(foldable.getClass(), IFoldable.FOLD_MAP_NAME, IMonoid.class, Function.class, foldable.getClassAtRuntime())
                            .map(m -> FoldableUtil.foldMap(foldable, new EndoMonoid<>(), (Function<M, Function<B, B>>) (M x) -> ((B y) -> function.apply(x, y)), fm).apply(b)));
        } else {
            return Optional.empty();
        }
    }

    public static <F, B> Optional<F> functorMapConst(IFunctor<? extends F> functor, F base, B constant) {
        if (isFunctor(functor.getClass())) {
            return getMethodIfExists(functor.getClass(), IFunctor.MAP_CONST_NAME, functor.getClassAtRuntime(), Object.class)
                    .<F>map(m -> invokeStaticMethod(functor, m, base, constant))
                    .or(() -> getMethodIfExists(functor.getClass(), IFunctor.MAP_NAME, functor.getClassAtRuntime(), Function.class)
                            .map(m -> FunctorUtil.map(functor, base, x -> constant)));
        } else {
            return Optional.empty();
        }
    }

    public static <F> Optional<F> applicativeKeepLeft(IApplicative<? extends F> applicative, F left, F right) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.KEEP_LEFT_NAME, applicative.getClassAtRuntime(), applicative.getClassAtRuntime())
                    .<F>map(m -> invokeStaticMethod(applicative, m, left, right))
                    .or(() -> Optional.of(ApplicativeUtil.liftA2(applicative, (x, y) -> x, left, right)));
        } else {
            return Optional.empty();
        }
    }

    public static <F> Optional<F> applicativeKeepRight(IApplicative<? extends F> applicative, F left, F right) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.KEEP_LEFT_NAME, applicative.getClassAtRuntime(), applicative.getClassAtRuntime())
                    .<F>map(m -> invokeStaticMethod(applicative, m, left, right))
                    .or(() -> Optional.of(ApplicativeUtil.fapply(applicative, right, FunctorUtil.mapConst(applicative, left, Function.identity()))));
        } else {
            return Optional.empty();
        }
    }

    public static <F, A, K> Optional<K> travesalTraverse(ITraversal<? extends F> traversal, IApplicative<? extends K> applicative, Function<A, ? extends K> mapping, F fa) {
        if (isTraversal(traversal.getClass())) {
            return getMethodIfExists(traversal.getClass(), ITraversal.TRAVERSE_NAME, IApplicative.class, Function.class, traversal.getClassAtRuntime())
                    .<K>map(m -> invokeStaticMethod(traversal, m, applicative, mapping, fa))
                    .or(() -> getMethodIfExists(traversal.getClass(), ITraversal.SEQUENCE_A_NAME, IApplicative.class, traversal.getClassAtRuntime())
                            .flatMap(m -> getMethodIfExists(traversal.getClass(), IFunctor.MAP_NAME, traversal.getClassAtRuntime(), Function.class))
                            .map(m -> TraversalUtil.sequenceA(traversal, applicative, FunctorUtil.map(traversal, fa, mapping)))
                    );
        } else {
            return Optional.empty();
        }
    }

    public static <F, A, B> Optional<F> traversalMap(IFunctor<? extends F> functor, F base, Function<A, B> map) {
        if (isTraversal(functor.getClass())) {
            ITraversal<? extends F> traversal = (ITraversal<? extends F>) functor;
            return getMethodIfExists(traversal.getClass(), IFunctor.MAP_NAME, functor.getClassAtRuntime(), Function.class)
                    .<F>map(m -> invokeStaticMethod(traversal, m, base, map))
                    .or(() -> getMethodIfExists(traversal.getClass(), ITraversal.TRAVERSE_NAME, IApplicative.class, Function.class, functor.getClassAtRuntime())
                            .or(() -> getMethodIfExists(traversal.getClass(), ITraversal.SEQUENCE_A_NAME, functor.getClassAtRuntime()))
                            .map(m -> ((Identity<F>) (Identity<?>) TraversalUtil.<Identity, F, A>traverse(traversal, new IdentityApplicative(), ((Function<B, Identity<B>>) IdentityApplicative::pure).compose(map), base)).get()));
        } else {
            return Optional.empty();
        }
    }

    public static <F, K> Optional<K> traversalSequenceA(ITraversal<? extends F> traversal, IApplicative<? extends K> applicative, F fa) {
        if (isTraversal(traversal.getClass())) {
            return getMethodIfExists(traversal.getClass(), ITraversal.SEQUENCE_A_NAME, IApplicative.class, traversal.getClassAtRuntime())
                    .<K>map(m -> invokeStaticMethod(traversal, m, applicative, fa))
                    .or(() -> getMethodIfExists(traversal.getClass(), ITraversal.TRAVERSE_NAME, IApplicative.class, Function.class, traversal.getClassAtRuntime())
                            .map(m -> TraversalUtil.traverse(traversal, applicative, Function.identity(), fa)));
        } else {
            return Optional.empty();
        }
    }

    public static <F> Optional<F> alternativeEmpty(IAlternative<? extends F> alternative) {
        if (isAlternative(alternative.getClass())) {
            return getMethodIfExists(alternative.getClass(), IAlternative.EMPTY_NAME)
                    .map(m -> invokeStaticMethod(alternative, m));
        } else {
            return Optional.empty();
        }
    }

    public static <F> Optional<F> alternativeDisj(IAlternative<? extends F> alternative, F fa, F fb) {
        if (isAlternative(alternative.getClass())) {
            return getMethodIfExists(alternative.getClass(), IAlternative.DISJ_NAME, alternative.getClassAtRuntime(), alternative.getClassAtRuntime())
                    .map(m -> invokeStaticMethod(alternative, m, fa, fb));
        } else {
            return Optional.empty();
        }
    }

    @Applicative
    public static class IdentityApplicative implements IApplicative<Identity<?>> {

        public static <A> Identity<A> pure(A a) {
            return new Identity<>(a);
        }

        public static <A, B> Identity<B> fapply(Identity<Function<A, B>> map, Identity<A> idA) {
            return new Identity<>(map.get().apply(idA.get()));
        }

        public static <A, B> Identity<B> map(Identity<A> ida, Function<A, B> map) {
            return new Identity<>(map.apply(ida.get()));
        }

        @Override
        public Class<Identity<?>> getClassAtRuntime() {
            return (Class<Identity<?>>) (Class<?>) Identity.class;
        }
    }

    private static class Identity<A> {
        private final A a;

        Identity(A a) {
            this.a = a;
        }

        A get() {
            return a;
        }
    }

    @Monoid
    public static class EndoMonoid<B> implements IMonoid<Function<B, B>> {
        public Function<B, B> op(Function<B, B> f1, Function<B, B> f2) {
            return f1.compose(f2);
        }

        public Function<B, B> unit() {
            return Function.identity();
        }
    }

    // SEMIGROUP
    // Operation
    static <A> Optional<A> semigroupOp(ISemigroup<? extends A> semigroup, A a, A b) {
        if (isSemigroup(semigroup.getClass())) {
            return Stream.of(semigroup.getClass().getGenericInterfaces())
                    .filter(iface -> iface.getTypeName().contains("IMonoid") || iface.getTypeName().contains("ISemigroup"))
                    .filter(iface -> iface instanceof ParameterizedType)
                    .findFirst()
                    .map(iface -> ((ParameterizedType) iface).getActualTypeArguments()[0])
                    .map(type -> {
                        if (type instanceof Class<?> cl) {
                            return cl;
                        } else if (type instanceof ParameterizedType ptype) {
                            return (Class<?>) ptype.getRawType();
                        }
                        return Object.class;
                    })
                    .flatMap(aClass -> getMethodIfExists(semigroup.getClass(), ISemigroup.OP_NAME, aClass, aClass))
                    .or(() -> getMethodIfExists(semigroup.getClass(), ISemigroup.OP_NAME, Object.class, Object.class))
                    .map(m -> invokeStaticMethod(semigroup, m, a, b));
        } else {
            return Optional.empty();
        }
    }

    // MONOID
    // Unit function
    static <A> Optional<A> monoidUnit(IMonoid<? extends A> monoid) {
        if (isMonoid(monoid.getClass())) {
            return getMethodIfExists(monoid.getClass(), IMonoid.UNIT_NAME)
                    .map(m -> invokeStaticMethod(monoid, m));
        } else {
            return Optional.empty();
        }
    }

    // FUNCTOR
    // Map functions
    static <F, A, B> Optional<F> applicativeMap(IFunctor<? extends F> functor, F base, Function<A, B> map) {
        if (isApplicative(functor.getClass())) {
            IApplicative<? extends F> applicative = (IApplicative<? extends F>) functor;
            return getMethodIfExists(applicative.getClass(), IApplicative.FAPPLY_NAME, functor.getClassAtRuntime(), functor.getClassAtRuntime())
                    .or(() -> getMethodIfExists(applicative.getClass(), IApplicative.LIFT_A2_NAME, BiFunction.class, functor.getClassAtRuntime(), functor.getClassAtRuntime()))
                    .map(m -> ApplicativeUtil.fapply(applicative, base, ApplicativeUtil.pure(applicative, map)));
        } else {
            return Optional.empty();
        }
    }

    static <F, A, B> Optional<F> monadMap(IFunctor<? extends F> functor, F base, Function<A, B> map) {
        if (isMonad(functor.getClass())) {
            IMonad<? extends F> monad = (IMonad<? extends F>) functor;
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, functor.getClassAtRuntime())
                    .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.PURE_NAME, Object.class))
                    .map(m -> MonadUtil.flatMap(monad, (A a) -> ApplicativeUtil.pure(monad, map.apply(a)), base));
        } else {
            return Optional.empty();
        }
    }

    static <F, A, B> Optional<F> functorMap(IFunctor<? extends F> functor, F base, Function<A, B> map) {
        if (isFunctor(functor.getClass())) {
            return getMethodIfExists(functor.getClass(), IFunctor.MAP_NAME, functor.getClassAtRuntime(), Function.class)
                    .map(m -> FunctionalUtil.invokeStaticMethod(functor, m, base, map));
        } else {
            return Optional.empty();
        }
    }

    // APPLICATIVE
    // Pure functions
    static <F, A> Optional<F> applicativePure(IApplicative<? extends F> applicative, A a) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.PURE_NAME, Object.class)
                    .map(m -> invokeStaticMethod(applicative, m, a));
        } else {
            return Optional.empty();
        }
    }

    // Fapply functions

    static <F> Optional<F> applicativeFapply(IApplicative<? extends F> applicative, F base, F ff) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.FAPPLY_NAME, applicative.getClassAtRuntime(), applicative.getClassAtRuntime())
                    .<F>map(m -> invokeStaticMethod(applicative, m, ff, base))
                    .or(() -> getMethodIfExists(applicative.getClass(), IApplicative.LIFT_A2_NAME, BiFunction.class, applicative.getClassAtRuntime(), applicative.getClassAtRuntime())
                            .map(m -> ApplicativeUtil.liftA2(applicative, (Function<Object, Object> a, Object b) -> a.apply(b), ff, base)));
        } else {
            return Optional.empty();
        }
    }

    static <F> Optional<F> monadFapply(IApplicative<? extends F> applicative, F base, F ff) {
        if (isMonad(applicative.getClass())) {
            IMonad<? extends F> monad = (IMonad<? extends F>) applicative;
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, applicative.getClassAtRuntime())
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, applicative.getClassAtRuntime())
                            .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.MAP_NAME, applicative.getClassAtRuntime(), Function.class)))
                    .map(m -> MonadUtil.flatMap(monad, f -> FunctorUtil.map(monad, base, (Function<?, ?>) f), ff));
        } else {
            return Optional.empty();
        }
    }

    // LiftA2 functions

    static <F, A, B, C> Optional<F> applicativeLiftA2(IApplicative<? extends F> applicative, BiFunction<A, B, C> mapping, F fa, F fb) {
        if (isApplicative(applicative.getClass())) {
            return getMethodIfExists(applicative.getClass(), IApplicative.LIFT_A2_NAME, BiFunction.class, applicative.getClassAtRuntime(), applicative.getClassAtRuntime())
                    .<F>map(m -> invokeStaticMethod(applicative, m, mapping, fa, fb))
                    .or(() -> getMethodIfExists(applicative.getClass(), IApplicative.FAPPLY_NAME, applicative.getClassAtRuntime(), applicative.getClassAtRuntime())
                            .map(m -> ApplicativeUtil.fapply(applicative, fb, FunctorUtil.map(applicative, fa, (A a) -> (Function<B, C>) (b -> mapping.apply(a, b))))));
        } else {
            return Optional.empty();
        }
    }

    static <F, A, B, C> Optional<F> monadLiftA2(IApplicative<? extends F> applicative, BiFunction<A, B, C> mapping, F fa, F fb) {
        if (isMonad(applicative.getClass())) {
            IMonad<? extends F> monad = (IMonad<? extends F>) applicative;
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, applicative.getClassAtRuntime())
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, applicative.getClassAtRuntime())
                            .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.MAP_NAME, applicative.getClassAtRuntime(), Function.class)))
                    .map(m -> ApplicativeUtil.fapply(monad, fb, FunctorUtil.map(monad, fa, (A a) -> (Function<B, C>) (b -> mapping.apply(a, b)))));
        } else {
            return Optional.empty();
        }
    }

    // MONAD
    // FlatMap functions

    static <F, A> Optional<F> monadFlatMap(IMonad<? extends F> monad, Function<A, F> mapping, F fa) {
        if (isMonad(monad.getClass())) {
            return getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, monad.getClassAtRuntime())
                    .<F>map(m -> invokeStaticMethod(monad, m, mapping, fa))
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, monad.getClassAtRuntime())
                            .flatMap(m -> getMethodIfExists(monad.getClass(), IMonad.MAP_NAME, monad.getClassAtRuntime(), Function.class))
                            .map(m -> MonadUtil.join(monad, FunctorUtil.map(monad, fa, mapping))));
        } else {
            return Optional.empty();
        }
    }

    // Join functions

    static <F> Optional<F> monadJoin(IMonad<? extends F> monad, F ffa) {
        if (isMonad(monad.getClass())) {
            return getMethodIfExists(monad.getClass(), IMonad.JOIN_NAME, monad.getClassAtRuntime())
                    .<F>map(m -> invokeStaticMethod(monad, m, ffa))
                    .or(() -> getMethodIfExists(monad.getClass(), IMonad.FLAT_MAP_NAME, Function.class, monad.getClassAtRuntime())
                            .map(m -> MonadUtil.flatMap(monad, Function.identity(), ffa)));
        } else {
            return Optional.empty();
        }
    }

}