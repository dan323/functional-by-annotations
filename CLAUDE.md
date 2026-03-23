# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Build all modules (skip tests)
mvn install -DskipTests

# Run all tests
mvn test -P test

# Run tests for a single module
mvn test -P test -pl example-functional
mvn test -P test -pl functional-definitions/functional-compiler

# Run a single test class
mvn test -P test -pl example-functional -Dtest=FiniteListTest

# Generate coverage report (JaCoCo)
mvn verify -P jacoco

# Mutation testing
mvn test pitest:mutationCoverage -pl functional-definitions/functional-compiler

# SonarQube analysis
mvn sonar:sonar
```

The `test` Maven profile is required for running tests — it configures JaCoCo and adds the necessary `--add-exports` JVM arg for the JPMS module system.

## Module Structure

Three top-level Maven modules:

- **`functional-definitions/annotation-definitions`** — Pure API layer. Defines the 9 annotations (`@Functor`, `@Applicative`, `@Monad`, `@Foldable`, `@Traversal`, `@Alternative`, `@Semigroup`, `@Monoid`, `@Ring`), the marker interfaces (`Functional<F>`, `Algebraic`, `ClassAware<F>`), and the structural interfaces (`IFunctor`, `IApplicative`, `IMonad`, `IFoldable`, `ITraversal`, `IAlternative`, `ISemigroup`, `IMonoid`, `IRing`). No dependencies.

- **`functional-definitions/functional-compiler`** — Compile-time annotation processor. `FunctionalCompiler` (extends `AbstractProcessor`) validates that annotated classes implement the required interface and provide the minimal set of required methods. It does not generate new source files — it validates only. SPI-registered in `module-info.java`.

- **`example-functional`** — Example implementations of functional data structures: `FiniteList`, `InfiniteList`, `Identity`, `Either`, `Optional`, `Continuation`, `Reader`, `Parser`, `Pair`, `Tree`, `Writer`, `State`, SQL types, and list zipper. The annotation processor is wired in via `annotationProcessorPaths` in its pom.xml.

- **`jacoco-functional`** — Coverage aggregator only (pom packaging, no source). Activated via `-P jacoco`.

Dependency direction: `annotation-definitions` ← `functional-compiler` ← `example-functional`.

## Architecture: How the Annotation Processor Works

The goal is to let users annotate a class (e.g., `@Monad`) and get a compile-time error if they forgot to implement the required minimal method set (e.g., `pure` + `flatMap`).

**Processing pipeline in `FunctionalCompiler`:**
1. For each annotated type, collect which `Functional`/`Algebraic` interfaces it directly implements via `CompilerUtils.getDirectFunctionalInterfaces()`.
2. `CompilerFactory.from()` maps each interface to a concrete `Compiler<A>` instance containing a structure-specific signature checker.
3. Each `Compiler.process()` walks the class hierarchy and verifies the required method signatures are present. Errors are emitted via `Messager`.

**Required methods per structure:**
- `@Functor` / `IFunctor` → `map`
- `@Applicative` / `IApplicative` → `pure` + (`fapply` or `liftA2`)
- `@Monad` / `IMonad` → `pure` + `flatMap`
- `@Foldable` / `IFoldable` → one of: `fold`, `foldMap`, `foldr`
- `@Traversal` / `ITraversal` → `traverse` or `sequenceA`
- `@Alternative` / `IAlternative` → `IApplicative` methods + `empty` + `disjunction`
- `@Semigroup` / `ISemigroup` → `op`
- `@Monoid` / `IMonoid` → `op` + `unit`
- `@Ring` / `IRing` → addition + multiplication operations

**Signature checking internals** live in `functional-compiler/src/main/java/.../compiler/internal/`: `CompilerFactory`, `Compiler`, `StructureSignatures`, and `NecessaryMethods` (conjunctive/disjunctive/empty states).

## JPMS (Java Module System)

All three source modules declare `module-info.java`. When writing tests in `example-functional` that need access to compiler-internal types, the `test` profile adds:
```
--add-exports=functional.data/com.dan323.mock=functional.compiler
```

The `functional.compiler` module uses `provides javax.annotation.processing.Processor with FunctionalCompiler` in its module-info to register the SPI.

## Java Version

The project targets Java 17 minimum and is tested on 17, 21, and 24 in CI. Profile-based compiler configuration in the root `pom.xml` handles version-specific settings. Use `--release 17` semantics when writing new code.
