# Architecture

## Overview

This project is a Maven multi-module build that separates annotations, the annotation processor, and examples. The compiler generates boilerplate methods based on minimal implementations declared with annotations.

## Module Map

```
functional-by-annotations/
├── functional-definitions/               (aggregator)
│   ├── annotation-definitions/           (annotations + interfaces)
│   └── functional-compiler/              (annotation processor)
├── example-functional/                   (sample data types + tests)
└── jacoco-functional/                    (coverage aggregation)
```

## Build-Time Flow

```
Source Types
   │  @Functor/@Monad/@Monoid...
   ▼
functional-compiler (annotation processor)
   │  validates minimal methods
   │  generates derived methods
   ▼
Generated code compiled with user sources
```

## Dependency Graph (Modules)

```
annotation-definitions
        ▲
        │
functional-compiler
        ▲
        │
example-functional
        │
        ▼
jacoco-functional
```

Notes:
- `functional-compiler` depends on `annotation-definitions`.
- `example-functional` uses annotations and the compiler for generated code.
- `jacoco-functional` aggregates coverage from examples and compiler.

## Key Design Decisions

### 1. Annotations separated from compiler
Keeping `annotation-definitions` independent allows users to reference annotations without pulling in compiler internals.

### 2. Compile-time generation
The compiler generates boilerplate (for example `map`, `fapply`, `flatMap`) based on minimal method requirements for each structure.

### 3. Explicit module boundaries
Java modules enforce clean API boundaries and prevent accidental package exposure.

## Structure Dependencies

```
IMonad -> IApplicative -> IFunctor
IMonoid -> ISemigroup
ITraversal -> IFunctor + IFoldable
IAlternative -> IApplicative
IRing -> ISemigroup (addition) + ISemigroup (multiplication)
```

## Where to Look

- Annotation definitions: `functional-definitions/annotation-definitions`
- Annotation processor: `functional-definitions/functional-compiler`
- Example implementations: `example-functional`
- CI/CD: `.github/workflows`

