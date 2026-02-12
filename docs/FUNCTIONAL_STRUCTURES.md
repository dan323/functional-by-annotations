# Functional Structures

Functional structures operate on type constructors like `F<A>`.

## Functor

**Purpose**: Transform values inside a context

**Required Methods**: `map`

**Laws**:
1. Identity: `map(id, fa) == fa`
2. Composition: `map(f∘g, fa) == map(f, map(g, fa))`

**Example**:
```java
@Functor
public class MaybeFunctor implements IFunctor<Maybe<?>> {
    public static <A, B> Maybe<B> map(Maybe<A> base, Function<A, B> f) {
        return base.maybe(a -> Maybe.of(f.apply(a)), Maybe.of());
    }
}
```

## Applicative

**Purpose**: Apply wrapped functions to wrapped values

**Required Methods**: `pure` + (`fapply` OR `liftA2`)

**Laws**:
1. Identity: `fapply(pure(id), fa) == fa`
2. Homomorphism: `fapply(pure(f), pure(x)) == pure(f(x))`

**Example**:
```java
@Applicative
public class MaybeApplicative implements IApplicative<Maybe<?>> {
    public static <A> Maybe<A> pure(A a) {
        return Maybe.of(a);
    }
    
    public static <A, B> Maybe<B> fapply(Maybe<Function<A,B>> ff, Maybe<A> fa) {
        return ff.maybe(f -> MaybeFunctor.map(fa, f), Maybe.of());
    }
}
```

## Monad

**Purpose**: Chain computations that produce wrapped values

**Required Methods**: `pure` + `flatMap`

**Laws**:
1. Left identity: `flatMap(f, pure(a)) == f(a)`
2. Right identity: `flatMap(pure, fa) == fa`
3. Associativity: `flatMap(g, flatMap(f, fa)) == flatMap(x -> flatMap(g, f(x)), fa)`

**Example**:
```java
@Monad
public class MaybeMonad implements IMonad<Maybe<?>> {
    public static <A> Maybe<A> pure(A a) {
        return Maybe.of(a);
    }
    
    public static <A, B> Maybe<B> flatMap(Function<A, Maybe<B>> f, Maybe<A> fa) {
        return fa.maybe(f, Maybe.of());
    }
}
```

## Foldable

**Purpose**: Reduce a structure to a single value

**Required Methods**: ONE of (`fold`, `foldMap`, `foldr`)

**Example**:
```java
@Foldable
public class ListFoldable implements IFoldable<FiniteList<?>> {
    public static <A, B> B foldr(BiFunction<A, B, B> f, B b, FiniteList<A> lst) {
        return lst.head().maybe(h -> foldr(f, f.apply(h, b), lst.tail()), b);
    }
}
```

## Traversal

**Purpose**: Map with effects while preserving structure

**Required Methods**: `traverse` OR `sequenceA` (+ Functor + Foldable)

**Example**:
```java
@Traversal
public class ListTraversal implements ITraversal<FiniteList<?>> {
    public static <K, A> K traverse(
            IApplicative<K> app, 
            Function<A, K> f, 
            FiniteList<A> lst) {
        K empty = ApplicativeUtil.pure(app, FiniteList.nil());
        return foldr((x, y) -> 
            ApplicativeUtil.liftA2(app, FiniteList::cons, f.apply(x), y), 
            empty, lst);
    }
}
```

## Alternative

**Purpose**: Provide choice operations

**Required Methods**: `empty` + `disjunction`

**Laws**:
1. Left identity: `disjunction(empty(), fa) == fa`
2. Right identity: `disjunction(fa, empty()) == fa`
3. Associativity: `disjunction(disjunction(fa, fb), fc) == disjunction(fa, disjunction(fb, fc))`

**Example**:
```java
@Alternative
public class MaybeAlternative implements IAlternative<Maybe<?>> {
    public static <A> Maybe<A> empty() {
        return Maybe.of();
    }
    
    public static <A> Maybe<A> disjunction(Maybe<A> first, Maybe<A> second) {
        return first.maybe(Maybe::of, second);
    }
}
```

---

**See also**: [Algebraic Structures](ALGEBRAIC_STRUCTURES.md) | [Examples](EXAMPLES.md) | [API Reference](API_REFERENCE.md)

