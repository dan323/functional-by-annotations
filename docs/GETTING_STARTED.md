# Getting Started with Functional Java by Annotations

## Quick Start

### Prerequisites

- **Java 24+**: The project requires Java 24 or later
- **Maven 3.6+**: For building the project

### Installation

#### 1. Add Dependencies

Add to your `pom.xml`:

```xml
<dependencies>
    <!-- Annotation definitions -->
    <dependency>
        <groupId>com.dan323</groupId>
        <artifactId>annotation-definitions</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    
    <!-- Compiler for code generation -->
    <dependency>
        <groupId>com.dan323</groupId>
        <artifactId>functional-compiler</artifactId>
        <version>1.0-SNAPSHOT</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

#### 2. Configure Annotation Processing

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>com.dan323</groupId>
                        <artifactId>functional-compiler</artifactId>
                        <version>1.0-SNAPSHOT</version>
                    </path>
                </annotationProcessorPaths>
                <annotationProcessors>
                    <annotationProcessor>
                        com.dan323.functional.annotation.compiler.FunctionalCompiler
                    </annotationProcessor>
                </annotationProcessors>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#### 3. Configure Module System (Optional)

If using Java modules:

```java
// module-info.java
module my.module {
    requires functional.annotations;
    requires functional.compiler;
    
    exports com.mycompany.functional;
}
```

## Your First Functor

### Step 1: Define Your Data Type

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
```

### Step 2: Implement the Functor

```java
import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import java.util.function.Function;

@Functor
public class MaybeFunctor implements IFunctor<Maybe<?>> {
    
    public static <A, B> Maybe<B> map(Maybe<A> base, Function<A, B> mapping) {
        return base.maybe(
            a -> Maybe.of(mapping.apply(a)),
            Maybe.of()
        );
    }
    
    @Override
    public Class<Maybe<?>> getClassAtRuntime() {
        return (Class<Maybe<?>>) (Class<?>) Maybe.class;
    }
}
```

### Step 3: Use It

```java
Maybe<Integer> value = Maybe.of(5);
Maybe<Integer> doubled = MaybeFunctor.map(value, x -> x * 2);
// Result: Just(10)

Maybe<Integer> empty = Maybe.of();
Maybe<Integer> stillEmpty = MaybeFunctor.map(empty, x -> x * 2);
// Result: Nothing
```

## Your First Monad

### Step 1: Implement the Monad Interface

```java
import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.funcs.IMonad;

@Monad
public class MaybeMonad implements IMonad<Maybe<?>> {
    
    // Pure: Lift a value into the context
    public static <A> Maybe<A> pure(A value) {
        return Maybe.of(value);
    }
    
    // FlatMap: Chain computations
    public static <A, B> Maybe<B> flatMap(
            Function<A, Maybe<B>> f, 
            Maybe<A> base) {
        return base.maybe(f, Maybe.of());
    }
    
    @Override
    public Class<Maybe<?>> getClassAtRuntime() {
        return (Class<Maybe<?>>) (Class<?>) Maybe.class;
    }
}
```

### Step 2: Use the Monad

```java
Maybe<Integer> value = MaybeMonad.pure(5);

// Chain operations
Maybe<Integer> result = MaybeMonad.flatMap(
    x -> MaybeMonad.pure(x * 2),
    value
);
// Result: Just(10)

// The compiler automatically generates map, fapply, join, etc.
Maybe<String> text = MaybeMonad.map(result, Object::toString);
// Result: Just("10")
```

## Your First Monoid

### Step 1: Implement the Monoid

```java
import com.dan323.functional.annotation.Monoid;
import com.dan323.functional.annotation.algs.IMonoid;

@Monoid
public final class SumMonoid implements IMonoid<Integer> {
    
    // Binary operation
    public static Integer op(Integer a, Integer b) {
        return a + b;
    }
    
    // Identity element
    public static Integer unit() {
        return 0;
    }
    
    private static final SumMonoid INSTANCE = new SumMonoid();
    
    public static SumMonoid getInstance() {
        return INSTANCE;
    }
}
```

### Step 2: Use the Monoid

```java
import com.dan323.functional.annotation.compiler.util.MonoidUtil;

SumMonoid monoid = SumMonoid.getInstance();

// Use the operations
Integer result = MonoidUtil.op(monoid, 5, 3);
// Result: 8

Integer identity = MonoidUtil.unit(monoid);
// Result: 0

// Fold a list using the monoid
Integer sum = List.of(1, 2, 3, 4, 5).stream()
    .reduce(MonoidUtil.unit(monoid), (a, b) -> MonoidUtil.op(monoid, a, b));
// Result: 15
```

## Understanding Minimal Implementations

The annotation processor is smart - you only need to implement the **minimum required methods**:

### Functor
- **Required**: `map`
- **Generated**: `mapConst`

### Applicative
- **Required**: `pure` + (`fapply` OR `liftA2`)
- **Generated**: The missing one, plus `map`, `keepLeft`, `keepRight`

### Monad
- **Required**: `pure` + (`flatMap` OR (`join` + minimal Functor/Applicative))
- **Generated**: All other methods including `map`, `fapply`, `join`/`flatMap`

### Foldable
- **Required**: ONE of (`fold`, `foldMap`, `foldr`)
- **Generated**: The other two methods

### Monoid
- **Required**: `op` + `unit`
- **Generated**: None (both required)

## Building the Project

```bash
# Clone the repository
git clone <repository-url>
cd functional-by-annotations

# Build all modules
mvn clean install

# Run tests
mvn test

# Run with coverage
mvn clean test -Ptest

# Generate coverage report
mvn clean verify -Pjacoco

# Run mutation testing
mvn clean test pitest:mutationCoverage
```

**For detailed pipeline information**: See [CI/CD Pipeline Documentation](PIPELINE.md)

## Next Steps

- **Learn the concepts**: Read [Core Concepts](CORE_CONCEPTS.md)
- **Explore annotations**: See [Annotations Reference](ANNOTATIONS.md)
- **Study examples**: Check [Implementation Examples](EXAMPLES.md)
- **Understand structures**: Read [Functional Structures](FUNCTIONAL_STRUCTURES.md) and [Algebraic Structures](ALGEBRAIC_STRUCTURES.md)
- **Deep dive**: See the [complete API reference](API_REFERENCE.md)

## Common Patterns

### Pattern 1: Optional Values (Maybe/Either)

```java
// Instead of null checks
String result = value != null ? value.toUpperCase() : "DEFAULT";

// Use Maybe
Maybe<String> result = MaybeMonad.map(maybeValue, String::toUpperCase);
```

### Pattern 2: Error Handling (Either)

```java
// Instead of try-catch everywhere
Either<Error, Result> result = computation()
    .flatMap(this::validate)
    .map(this::process);
```

### Pattern 3: List Operations (FiniteList)

```java
// Functional list operations
FiniteList<Integer> numbers = FiniteList.of(1, 2, 3, 4, 5);
FiniteList<Integer> doubled = FiniteListFunctional.map(numbers, x -> x * 2);
Integer sum = FiniteListFunctional.foldr(Integer::sum, 0, numbers);
```

## Troubleshooting

### Compilation Fails

**Problem**: "The functor is not correctly implemented"

**Solution**: Make sure you've implemented at least the minimal required methods. Check the [Annotations Reference](ANNOTATIONS.md) for requirements.

### Annotation Processor Not Running

**Problem**: No methods are being generated

**Solution**: Verify your `maven-compiler-plugin` configuration includes the annotation processor path and processor class.

### Module System Issues

**Problem**: "Package not found" errors

**Solution**: Add `requires functional.annotations;` and `requires functional.compiler;` to your `module-info.java`.

## Getting Help

- 📚 **Documentation**: Check the [docs](.) folder
- 🐛 **Issues**: See [CONTRIBUTING.md](../CONTRIBUTING.md)
- 📖 **Examples**: Browse the `example-functional` module
- 🔧 **Upgrade**: See [Java 24 Upgrade Guide](JAVA24_UPGRADE.md)

---

**Ready to dive deeper?** Continue to [Core Concepts](CORE_CONCEPTS.md)

