# API Reference

## Annotations

| Annotation | Target | Purpose |
|------------|--------|---------|
| `@Functor` | Type | Mark as functor |
| `@Applicative` | Type | Mark as applicative |
| `@Monad` | Type | Mark as monad |
| `@Foldable` | Type | Mark as foldable |
| `@Traversal` | Type | Mark as traversal |
| `@Alternative` | Type | Mark as alternative |
| `@Semigroup` | Type | Mark as semigroup |
| `@Monoid` | Type | Mark as monoid |
| `@Ring` | Type | Mark as ring |

## Interfaces

### Functor
- `map(F<A> fa, Function<A,B> f) -> F<B>`
- `mapConst(F<A> fa, B b) -> F<B>`

### Applicative (extends Functor)
- `pure(A a) -> F<A>`
- `fapply(F<Function<A,B>> ff, F<A> fa) -> F<B>`
- `liftA2(BiFunction<A,B,C> f, F<A> fa, F<B> fb) -> F<C>`
- `keepLeft(F<A> fa, F<B> fb) -> F<A>`
- `keepRight(F<A> fa, F<B> fb) -> F<B>`

### Monad (extends Applicative)
- `flatMap(Function<A,F<B>> f, F<A> fa) -> F<B>`
- `join(F<F<A>> ffa) -> F<A>`

### Foldable
- `fold(IMonoid<A> m, F<A> fa) -> A`
- `foldMap(IMonoid<M> m, Function<A,M> f, F<A> fa) -> M`
- `foldr(BiFunction<A,B,B> f, B b, F<A> fa) -> B`

### Traversal (extends Functor + Foldable)
- `traverse(IApplicative<G> g, Function<A,G<B>> f, F<A> fa) -> G<F<B>>`
- `sequenceA(IApplicative<G> g, F<G<A>> fga) -> G<F<A>>`

### Alternative (extends Applicative)
- `empty() -> F<A>`
- `disjunction(F<A> fa1, F<A> fa2) -> F<A>`
- `some(F<A> fa) -> F<List<A>>`
- `many(F<A> fa) -> F<List<A>>`

### Semigroup
- `op(A a, A b) -> A`

### Monoid (extends Semigroup)
- `unit() -> A`

## Utility Classes

### FunctorUtil
```java
public static <F, A, B> F map(IFunctor<? extends F> functor, F base, Function<A, B> function)
public static <F, B> F mapConst(IFunctor<? extends F> functor, F base, B constant)
```

### ApplicativeUtil
```java
public static <F, A> F pure(IApplicative<? extends F> applicative, A a)
public static <F> F fapply(IApplicative<? extends F> applicative, F base, F ff)
public static <F, A, B, C> F liftA2(IApplicative<? extends F> applicative, BiFunction<A, B, C> map, F fa, F fb)
public static <F> F keepLeft(IApplicative<? extends F> applicative, F left, F right)
public static <F> F keepRight(IApplicative<? extends F> applicative, F left, F right)
```

### MonadUtil
```java
public static <F, A> F flatMap(IMonad<? extends F> monad, Function<A, F> mapping, F fa)
public static <F> F join(IMonad<? extends F> monad, F ffa)
```

### SemigroupUtil
```java
public static <A> A op(ISemigroup<? extends A> semi, A a, A b)
```

### MonoidUtil
```java
public static <A> A unit(IMonoid<? extends A> monoid)
public static <A> A op(IMonoid<? extends A> monoid, A a, A b)
```

### FoldableUtil
```java
public static <F, A> A fold(IFoldable<? extends F> foldable, IMonoid<? extends A> monoid, F a)
public static <F, A, M> M foldMap(IFoldable<? extends F> foldable, IMonoid<? extends M> monoid, Function<A,M> function, F base)
public static <F, A, B> B foldr(IFoldable<? extends F> foldable, BiFunction<A,B,B> function, B b, F fa)
```

### TraversalUtil
```java
public static <K, F, A> K traverse(ITraversal<? extends F> traversal, IApplicative<? extends K> applicative, Function<A, ? extends K> mapping, F fa)
public static <K, F> K sequenceA(ITraversal<? extends F> traversal, IApplicative<? extends K> applicative, F fa)
```

### AlternativeUtil
```java
public static <F> F empty(IAlternative<? extends F> alternative)
public static <F> F disjunction(IAlternative<? extends F> alternative, F fa, F fb)
```

## Data Structures (Examples)

### Identity
```java
Identity<A>
  - runIdentity(Identity<A>) -> A
```

### Maybe
```java
Maybe<A>
  - of(A) -> Maybe<A>
  - of() -> Maybe<A>  // Nothing
  - maybe(Function<A,C>, C) -> C
```

### Either
```java
Either<A,B>
  - left(A) -> Either<A,B>
  - right(B) -> Either<A,B>
  - either(Function<A,C>, Function<B,C>) -> C
```

### FiniteList
```java
FiniteList<A>
  - nil() -> FiniteList<A>
  - cons(A, FiniteList<A>) -> FiniteList<A>
  - of(A...) -> FiniteList<A>
  - head() -> Maybe<A>
  - tail() -> FiniteList<A>
  - length() -> int
```

### Pair
```java
Pair<A,B>
  - new Pair(A, B)
  - getKey() -> A
  - getValue() -> B
  - mapFirst(Function<A,C>) -> Pair<C,B>
  - mapSecond(Function<B,C>) -> Pair<A,C>
```

### BinaryTree
```java
BinaryTree<A>
  - leaf() -> BinaryTree<A>
  - node(BinaryTree<A>, A, BinaryTree<A>) -> BinaryTree<A>
```

### Continuation
```java
Continuation<A,R>
  - apply(Function<A,R>) -> R
```

### State
```java
State<A,S>
  - fromFun(Function<S,Pair<A,S>>) -> State<A,S>
  - apply(S) -> Either<Void, Pair<A,S>>
```

### Writer
```java
Writer<A,W>
  - tell(W) -> Writer<Unit,W>
  - log() -> W
  - execute() -> A
```

## Compiler

### FunctionalCompiler

Annotation processor that:
- Validates implementations at compile-time
- Generates missing methods from minimal implementations
- Reports errors for invalid structures

**Supported Annotations**:
- `@Functor`, `@Applicative`, `@Monad`
- `@Foldable`, `@Traversal`
- `@Alternative`
- `@Semigroup`, `@Monoid`, `@Ring`

**Usage**: Add as annotation processor in `maven-compiler-plugin` configuration.

---

**See also**: [Getting Started](GETTING_STARTED.md) | [Examples](EXAMPLES.md)

