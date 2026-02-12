# Implementation Examples

## Example 1: Maybe (Optional Values)

### Data Type
```java
public sealed interface Maybe<A> permits Nothing, Just {
    <C> C maybe(Function<A, C> f, C constant);
    
    static <A> Maybe<A> of(A element) {
        return new Just<>(element);
    }
    
    static <A> Maybe<A> of() {
        return (Maybe<A>) Nothing.NOTHING;
    }
}

record Just<A>(A value) implements Maybe<A> {
    public <C> C maybe(Function<A, C> f, C constant) {
        return f.apply(value);
    }
}

record Nothing<A>() implements Maybe<A> {
    static final Nothing<?> NOTHING = new Nothing<>();
    
    public <C> C maybe(Function<A, C> f, C constant) {
        return constant;
    }
}
```

### Monad Implementation
```java
@Monad
public class MaybeMonad implements IMonad<Maybe<?>> {
    public static <A> Maybe<A> pure(A a) {
        return Maybe.of(a);
    }
    
    public static <A, B> Maybe<B> flatMap(Function<A, Maybe<B>> f, Maybe<A> fa) {
        return fa.maybe(f, Maybe.of());
    }
    
    @Override
    public Class<Maybe<?>> getClassAtRuntime() {
        return (Class<Maybe<?>>) (Class<?>) Maybe.class;
    }
}
```

### Usage
```java
Maybe<Integer> value = MaybeMonad.pure(5);
Maybe<Integer> doubled = MaybeMonad.map(value, x -> x * 2);
Maybe<String> text = MaybeMonad.flatMap(
    x -> x > 10 ? Maybe.of("large") : Maybe.of(), 
    doubled
);
```

## Example 2: Either (Error Handling)

### Data Type
```java
public sealed interface Either<A, B> permits Left, Right {
    <C> C either(Function<A, C> left, Function<B, C> right);
    
    static <A, B> Either<A, B> left(A a) {
        return new Left<>(a);
    }
    
    static <A, B> Either<A, B> right(B b) {
        return new Right<>(b);
    }
}

record Left<A, B>(A value) implements Either<A, B> {
    public <C> C either(Function<A, C> left, Function<B, C> right) {
        return left.apply(value);
    }
}

record Right<A, B>(B value) implements Either<A, B> {
    public <C> C either(Function<A, C> left, Function<B, C> right) {
        return right.apply(value);
    }
}
```

### Monad Implementation (Right-biased)
```java
@Monad
public class RightEither<L> implements IMonad<Either<L, ?>> {
    public static <L, R> Either<L, R> pure(R r) {
        return Either.right(r);
    }
    
    public static <L, R, S> Either<L, S> flatMap(
            Function<R, Either<L, S>> f, 
            Either<L, R> base) {
        return base.either(Either::left, f);
    }
    
    @Override
    public Class<Either<L, ?>> getClassAtRuntime() {
        return (Class<Either<L, ?>>) (Class<?>) Either.class;
    }
}
```

### Usage
```java
Either<String, Integer> result = parseNumber("42");
Either<String, Integer> doubled = RightEither.map(result, x -> x * 2);
Either<String, String> formatted = RightEither.map(doubled, x -> "Result: " + x);
// Right("Result: 84")

Either<String, Integer> error = parseNumber("invalid");
Either<String, String> stillError = RightEither.map(error, x -> "Result: " + x);
// Left("Parse error")
```

## Example 3: FiniteList (Functional Lists)

### Monad + Traversal + Alternative
```java
@Monad
@Traversal
@Alternative
public class FiniteListFunctional implements 
        IMonad<FiniteList<?>>,
        ITraversal<FiniteList<?>>,
        IAlternative<FiniteList<?>> {
    
    // Functor
    public static <A, B> FiniteList<B> map(
            FiniteList<A> list, 
            Function<A, B> f) {
        return list.head().maybe(
            h -> FiniteList.cons(f.apply(h), map(list.tail(), f)),
            FiniteList.nil()
        );
    }
    
    // Applicative
    public static <A> FiniteList<A> pure(A a) {
        return FiniteList.of(a);
    }
    
    // Monad
    public static <A, B> FiniteList<B> flatMap(
            Function<A, FiniteList<B>> f, 
            FiniteList<A> list) {
        return list.head().maybe(
            h -> concat(f.apply(h), flatMap(f, list.tail())),
            FiniteList.nil()
        );
    }
    
    // Foldable
    public static <A, B> B foldr(
            BiFunction<A, B, B> f, 
            B b, 
            FiniteList<A> list) {
        return list.head().maybe(
            h -> foldr(f, f.apply(h, b), list.tail()),
            b
        );
    }
    
    // Traversal
    public static <K, A> K traverse(
            IApplicative<K> app,
            Function<A, K> f,
            FiniteList<A> list) {
        K empty = ApplicativeUtil.pure(app, FiniteList.nil());
        return foldr(
            (x, y) -> ApplicativeUtil.liftA2(app, FiniteList::cons, f.apply(x), y),
            empty,
            list
        );
    }
    
    // Alternative
    public static <A> FiniteList<A> empty() {
        return FiniteList.nil();
    }
    
    public static <A> FiniteList<A> disjunction(
            FiniteList<A> first, 
            FiniteList<A> second) {
        return concat(first, second);
    }
    
    @Override
    public Class<FiniteList<?>> getClassAtRuntime() {
        return (Class<FiniteList<?>>) (Class) FiniteList.class;
    }
}
```

### Usage
```java
FiniteList<Integer> numbers = FiniteList.of(1, 2, 3, 4, 5);

// Map
FiniteList<Integer> doubled = FiniteListFunctional.map(numbers, x -> x * 2);
// [2, 4, 6, 8, 10]

// FlatMap
FiniteList<Integer> repeated = FiniteListFunctional.flatMap(
    x -> FiniteList.of(x, x),
    numbers
);
// [1, 1, 2, 2, 3, 3, 4, 4, 5, 5]

// Fold
Integer sum = FiniteListFunctional.foldr(Integer::sum, 0, numbers);
// 15

// Alternative (concatenation)
FiniteList<Integer> combined = FiniteListFunctional.disjunction(
    FiniteList.of(1, 2, 3),
    FiniteList.of(4, 5, 6)
);
// [1, 2, 3, 4, 5, 6]
```

## Example 4: Identity Monad

### Simplest Monad
```java
@Monad
public class IdentityMonad implements IMonad<Identity<?>> {
    public static <A> Identity<A> pure(A a) {
        return new Identity<>(a);
    }
    
    public static <A, B> Identity<B> map(Identity<A> id, Function<A, B> f) {
        return pure(f.apply(Identity.runIdentity(id)));
    }
    
    public static <A> Identity<A> join(Identity<Identity<A>> iid) {
        return Identity.runIdentity(iid);
    }
    
    public static <A, B> Identity<B> flatMap(
            Function<A, Identity<B>> f, 
            Identity<A> id) {
        return f.apply(Identity.runIdentity(id));
    }
    
    @Override
    public Class<Identity<?>> getClassAtRuntime() {
        return (Class<Identity<?>>) (Class<?>) Identity.class;
    }
}
```

## Example 5: Function Monad

### Reader Monad Pattern
```java
@Monad
public class FunctionFrom<A> implements IMonad<Function<A, ?>> {
    public <B, C> Function<A, C> map(Function<A, B> base, Function<B, C> f) {
        return f.compose(base);
    }
    
    public <B> Function<A, B> pure(B data) {
        return x -> data;
    }
    
    public <B, C> Function<A, C> fapply(
            Function<A, Function<B, C>> ff, 
            Function<A, B> g) {
        return x -> ff.apply(x).apply(g.apply(x));
    }
    
    public <B, C> Function<A, C> flatMap(
            Function<B, Function<A, C>> f, 
            Function<A, B> base) {
        return x -> f.apply(base.apply(x)).apply(x);
    }
    
    @Override
    public Class<Function<A, ?>> getClassAtRuntime() {
        return (Class<Function<A, ?>>) (Class<?>) Function.class;
    }
}
```

### Usage
```java
// Configuration reader pattern
record Config(String host, int port, boolean ssl) {}

FunctionFrom<Config> monad = FunctionFrom.getInstance();

Function<Config, String> getHost = Config::host;
Function<Config, Integer> getPort = Config::port;

Function<Config, String> getUrl = monad.liftA2(
    (host, port) -> "http://" + host + ":" + port,
    getHost,
    getPort
);

Config config = new Config("localhost", 8080, false);
String url = getUrl.apply(config);
// "http://localhost:8080"
```

## Example 6: Continuation Monad

### Advanced Control Flow
```java
@Monad
public class ContinuationMonad<R> implements IMonad<Continuation<?, R>> {
    public <A, B> Continuation<B, R> map(
            Continuation<A, R> base, 
            Function<A, B> f) {
        return k -> base.apply(k.compose(f));
    }
    
    public <A> Continuation<A, R> pure(A a) {
        return k -> k.apply(a);
    }
    
    public <A, B> Continuation<B, R> fapply(
            Continuation<Function<A, B>, R> ff, 
            Continuation<A, R> ca) {
        return k -> ff.apply(f -> ca.apply(k.compose(f)));
    }
    
    public <A> Continuation<A, R> join(
            Continuation<Continuation<A, R>, R> cc) {
        return k -> cc.apply(c -> c.apply(k));
    }
    
    @Override
    public Class<Continuation<?, R>> getClassAtRuntime() {
        return (Class<Continuation<?, R>>) (Class<?>) Continuation.class;
    }
}
```

## Example 7: Boolean Monoids

### AND Monoid
```java
@Monoid
public class AndMonoid implements IMonoid<Boolean> {
    public static Boolean op(Boolean a, Boolean b) {
        return a && b;
    }
    
    public static Boolean unit() {
        return true;
    }
}
```

### OR Monoid
```java
@Monoid
public class OrMonoid implements IMonoid<Boolean> {
    public static Boolean op(Boolean a, Boolean b) {
        return a || b;
    }
    
    public static Boolean unit() {
        return false;
    }
}
```

### Usage
```java
// Check if all conditions are true
List<Boolean> conditions = List.of(true, true, false, true);
Boolean allTrue = conditions.stream()
    .reduce(AndMonoid.unit(), AndMonoid::op);
// false

// Check if any condition is true
Boolean anyTrue = conditions.stream()
    .reduce(OrMonoid.unit(), OrMonoid::op);
// true
```

## Example 8: Integer Monoids

### Sum Monoid
```java
@Monoid
public class SumMonoid implements IMonoid<Integer> {
    public static Integer op(Integer a, Integer b) {
        return a + b;
    }
    
    public static Integer unit() {
        return 0;
    }
}
```

### Product Monoid
```java
@Monoid
public class ProductMonoid implements IMonoid<Integer> {
    public static Integer op(Integer a, Integer b) {
        return a * b;
    }
    
    public static Integer unit() {
        return 1;
    }
}
```

---

**See also**: [Functional Structures](FUNCTIONAL_STRUCTURES.md) | [Algebraic Structures](ALGEBRAIC_STRUCTURES.md) | [API Reference](API_REFERENCE.md)

