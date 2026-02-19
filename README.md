# Functional Java by Annotations

[![Java Version](https://img.shields.io/badge/Java-17%2B-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![Release](https://img.shields.io/github/v/release/dan323/functional-by-annotations)](https://github.com/dan323/functional-by-annotations/releases)
[![License](https://img.shields.io/github/license/dan323/functional-by-annotations)](LICENSE)

[![Main Workflow](https://github.com/dan323/functional-by-annotations/workflows/Main%20Workflow/badge.svg)](https://github.com/dan323/functional-by-annotations/actions)
[![GitHub issues](https://img.shields.io/github/issues/dan323/functional-by-annotations)](https://github.com/dan323/functional-by-annotations/issues)
[![GitHub stars](https://img.shields.io/github/stars/dan323/functional-by-annotations)](https://github.com/dan323/functional-by-annotations/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/dan323/functional-by-annotations)](https://github.com/dan323/functional-by-annotations/network)
[![Last Commit](https://img.shields.io/github/last-commit/dan323/functional-by-annotations)](https://github.com/dan323/functional-by-annotations/commits)

[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=dan323_functional-by-annotations&metric=alert_status)](https://sonarcloud.io/dashboard?id=dan323_functional-by-annotations)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=dan323_functional-by-annotations&metric=coverage)](https://sonarcloud.io/dashboard?id=dan323_functional-by-annotations)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=dan323_functional-by-annotations&metric=bugs)](https://sonarcloud.io/dashboard?id=dan323_functional-by-annotations)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=dan323_functional-by-annotations&metric=code_smells)](https://sonarcloud.io/dashboard?id=dan323_functional-by-annotations)

> Bring functional programming concepts to Java through annotations and compile-time code generation

## Java Version Support

- **v1.3 (current):** Java 24
- **v2.0 (production-ready, targeting May 2026):** Java 17+ minimum (tested on 17, 21, 24)
  - See [Breaking Changes: v1.3 → v2.0](docs/BREAKING_CHANGES.md) for migration details

## Distribution

- **Maven Central:** Release artifacts are published under `io.github.dan323` (no extra repositories required).

## What is this?

**Functional Java by Annotations** makes functional programming in Java easy by using annotations to automatically generate boilerplate code. Define your functors, monads, and other functional structures with minimal code - the compiler does the rest.

```java
@Monad
public class MaybeMonad implements IMonad<Maybe<?>> {
    public static <A> Maybe<A> pure(A value) { return Maybe.of(value); }
    public static <A, B> Maybe<B> flatMap(Function<A, Maybe<B>> f, Maybe<A> fa) {
        return fa.maybe(f, Maybe.of());
    }
    // Compiler generates: map, join, fapply, liftA2, keepLeft, keepRight
}
```

## 📚 Documentation Index

Welcome to the complete documentation for **Functional Java by Annotations**. This library brings functional programming concepts from category theory to Java through annotations and compile-time code generation.

### 🚀 Getting Started

**Start here if you're new to the library**

- **[Getting Started Guide](docs/GETTING_STARTED.md)**
    - Installation and setup
    - Your first Functor, Monad, and Monoid
    - Common patterns and troubleshooting
    - Quick examples to get you productive

### 🧠 Core Documentation

**Understanding the library**

- **[Core Concepts](docs/CORE_CONCEPTS.md)**
    - Type hierarchy and structure
    - Functional vs Algebraic structures
    - Minimal implementations
    - Type constructors and variance

- **[Functional Structures](docs/FUNCTIONAL_STRUCTURES.md)**
    - Functor, Applicative, Monad
    - Foldable, Traversal
    - Alternative
    - Laws and requirements

- **[Algebraic Structures](docs/ALGEBRAIC_STRUCTURES.md)**
    - Semigroup, Monoid
    - Ring
    - Laws and examples

### 📖 Practical Guides

**Real-world usage**

- **[Implementation Examples](docs/EXAMPLES.md)**
    - Maybe (Optional values)
    - Either (Error handling)
    - FiniteList (Functional lists)
    - Identity, Continuation, Function monads
    - Boolean and Integer monoids
    - Complete implementations with usage

- **[API Reference](docs/API_REFERENCE.md)**
    - All annotations
    - Interface methods
    - Utility classes
    - Data structures
    - Quick reference tables

- **[Parser Combinators](docs/PARSER.md)**
    - Character and string parsers
    - Parser combinators (optional, many, some, disjunction)
    - Lookahead and error handling
    - Best practices for parser composition

### 🧭 Architecture & Build

- **[Architecture](docs/ARCHITECTURE.md)**
- **[Building](docs/BUILDING.md)**

### 📋 v2.0 Release Planning (Feb-May 2026)

**Upgrading from v1.3?** Read the migration guides:
- **[Breaking Changes Guide](docs/BREAKING_CHANGES.md)** - All v1.3 → v2.0 breaking changes with detailed migration paths
- **[API Audit & Stability Matrix](docs/API_AUDIT_v2.0.md)** - Complete inventory of public APIs and stability classification
- **[v2.0 Production Readiness Plan](docs/PLAN.md)** - 16-week comprehensive roadmap to production-ready status

## 📖 Quick Reference

### Annotations

| Annotation | Purpose | Minimal Required |
|------------|---------|------------------|
| `@Functor` | Transform values in context | `map` |
| `@Applicative` | Apply wrapped functions | `pure` + (`fapply` OR `liftA2`) |
| `@Monad` | Chain computations | `pure` + `flatMap` |
| `@Foldable` | Reduce structures | ONE of (`fold`, `foldMap`, `foldr`) |
| `@Traversal` | Map with effects | `traverse` OR `sequenceA` |
| `@Alternative` | Choice operations | `empty` + `disjunction` |
| `@Semigroup` | Associative operation | `op` |
| `@Monoid` | Semigroup with identity | `op` + `unit` |
| `@Ring` | Addition & multiplication | Both operations |

### Quick Links

- 🧭 [Architecture](docs/ARCHITECTURE.md)
- 🏗️ [Building](docs/BUILDING.md)
- 🎯 **[v2.0 Roadmap](docs/PLAN.md)** - Production-ready status roadmap
- 📝 [Contributing Guidelines](CONTRIBUTING.md)
- 🔧 [CI/CD Pipeline](docs/PIPELINE.md)
- 📜 [Changelog](CHANGELOG.md)
- 📄 [License](LICENSE)
- 🤝 [Code of Conduct](CODE_OF_CONDUCT.md)

## 🎯 Learning Path

### For Beginners

1. Read [Getting Started](docs/GETTING_STARTED.md)
1. Understand [Core Concepts](docs/CORE_CONCEPTS.md)
1. Study [Examples](docs/EXAMPLES.md)
1. Try implementing your own structures

### For Experienced Developers

1. Jump to [Functional Structures](docs/FUNCTIONAL_STRUCTURES.md)
1. Review [Algebraic Structures](docs/ALGEBRAIC_STRUCTURES.md)
1. Reference [API Documentation](docs/API_REFERENCE.md)
1. Explore the `example-functional` module

### For Contributors

1. Read [Contributing Guidelines](CONTRIBUTING.md)
1. Check [Testing Requirements](CONTRIBUTING.md#testing-requirements)
1. Understand mutation testing (85%+ coverage)

## 🔍 What's Where?

### Installation & Setup
→ [Getting Started](docs/GETTING_STARTED.md#installation)

### Understanding Functors
→ [Functional Structures - Functor](docs/FUNCTIONAL_STRUCTURES.md#functor)

### Understanding Monads
→ [Functional Structures - Monad](docs/FUNCTIONAL_STRUCTURES.md#monad)

### Maybe/Optional Pattern
→ [Examples - Maybe](docs/EXAMPLES.md#example-1-maybe-optional-values)

### Error Handling with Either
→ [Examples - Either](docs/EXAMPLES.md#example-2-either-error-handling)

### Working with Lists
→ [Examples - FiniteList](docs/EXAMPLES.md#example-3-finitelist-functional-lists)

### Combining Values (Monoids)
→ [Algebraic Structures - Monoid](docs/ALGEBRAIC_STRUCTURES.md#monoid)

### Complete API
→ [API Reference](docs/API_REFERENCE.md)

## 📊 Project Statistics

- **Minimum Java Version**: 17 (LTS)
- **Tested Versions**: Java 17 LTS, 21 LTS, 24
- **Modules**: 3 (annotations, compiler, examples)
- **Annotations**: 9 functional + algebraic structures
- **Test Coverage**: Target 85%+ with mutation testing
- **Build Tool**: Maven 3.6+

## 🎓 Category Theory Background

This library is inspired by concepts from category theory:

- **Functor**: A mapping between categories preserving structure
- **Applicative**: Functors with the ability to apply functions within structure
- **Monad**: Structures that allow chaining of computations
- **Semigroup**: A set with an associative binary operation
- **Monoid**: A semigroup with an identity element

### Further Reading

- "Functional Programming in Java" by Venkat Subramaniam
- "Category Theory for Programmers" by Bartosz Milewski
- Haskell documentation
- Scala Cats library

## 🔗 Related Projects

- **[Vavr](https://www.vavr.io/)**: Functional programming library for Java
- **[Cats](https://typelevel.org/cats/)**: Category theory library for Scala
- **[fp-ts](https://gcanti.github.io/fp-ts/)**: Functional programming in TypeScript

## 💬 Getting Help

- 📚 Browse this documentation
- 🐛 [Report Issues](CONTRIBUTING.md)
- 💡 Ask questions in GitHub Discussions
- 📖 Check the [examples module](example-functional)

## 🏗️ Project Structure

```
functional-by-annotations/
├── functional-definitions/
│   ├── annotation-definitions/     # @Functor, @Monad, etc.
│   └── functional-compiler/        # Annotation processor
├── example-functional/             # Example implementations
│   ├── src/main/java/.../data/    # Data structures
│   └── src/test/java/             # Tests
├── jacoco-functional/             # Coverage aggregation
└── docs/                          # This documentation
    ├── README.md                  # This file
    ├── GETTING_STARTED.md
    ├── CORE_CONCEPTS.md
    ├── FUNCTIONAL_STRUCTURES.md
    ├── ALGEBRAIC_STRUCTURES.md
    ├── EXAMPLES.md
    ├── API_REFERENCE.md
    └── JAVA24_UPGRADE.md
```

## 🎉 Quick Start Example

```java
// 1. Define your data type
public sealed interface Maybe<A> permits Nothing, Just {
    <C> C maybe(Function<A, C> f, C constant);
}

// 2. Add annotation and minimal implementation
@Monad
public class MaybeMonad implements IMonad<Maybe<?>> {
    public static <A> Maybe<A> pure(A a) {
        return Maybe.of(a);
    }
    
    public static <A, B> Maybe<B> flatMap(Function<A, Maybe<B>> f, Maybe<A> fa) {
        return fa.maybe(f, Maybe.of());
    }
}

// 3. Use it - the compiler generated map, join, fapply, etc.
Maybe<Integer> result = MaybeMonad.flatMap(
    x -> x > 10 ? Maybe.of(x) : Maybe.of(),
    MaybeMonad.map(MaybeMonad.pure(5), x -> x * 2)
);
```

---

**Ready to dive in?** Start with the [Getting Started Guide](docs/GETTING_STARTED.md)!
