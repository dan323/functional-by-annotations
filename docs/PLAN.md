# v1.3 → v2.0 Roadmap
## functional-by-annotations

**Current Version:** 1.3-SNAPSHOT
**Target Version:** 2.0
**Plan Written:** February 18, 2026
**Plan Revised:** March 23, 2026
**Target Release:** May 31, 2026

---

## Status as of March 23, 2026

### Completed
- Java 17+ minimum enforced; CI tests Java 17, 21, 24
- `ConcurrentTypeClassSanityTest` added for annotation processor thread safety
- `docs/API_AUDIT_v2.0.md` and `docs/BREAKING_CHANGES.md` created
- SQL module scaffolded: `SqlAst`, `Expr`, `Query`, `QueryFunctor`, `RowDecoder`, `Config`, `DbError`
- `ExprCompiler` — converts typed `Expr<A>` AST to SQL strings
- `SqlAstCompiler` — converts `SqlAst` to optimised SQL strings using a `Fragment` intermediate representation (Filter/Project fold; no unnecessary subqueries)
- `RowDecoder` primitive factories: `string`, `integer`, `longCol`, `bool`
- `QueryFunctor`: `pure`, `map`, `empty`, `disjunction`, `liftA2` (CROSS JOIN), `join` (JOIN…ON)
- List overhaul in progress on `feature/overhaul-lists`: `Cycle`, `Merged`, `ZipApplicative` rework, `ListZipper` fixes
- Test suite extended: `SqlAstCompilerTest` (24 tests), `RowDecoderTest` (18 tests), `QueryFunctorTest` (6 tests), `SqlRunnerTest` (9 tests), `RowDecoderMonadTest` (10 tests), `SqlAstMonoidTest` (6 tests)
- Pre-existing compilation bugs fixed: missing `List` import in 6 test/source files
- `SqlRunner` — JDBC execution layer with `run(Config, Query<A>)` and `run(Connection, Query<A>)` overloads
- `RowDecoderMonad` — `@Monad @Alternative` type class instance for `RowDecoder<?>`
- `SqlAstMonoid` — `@Monoid` type class instance for `SqlAst` (UNION monoid)

### Not Started (original plan items dropped)
The following items from the original plan are removed as out of scope for this project:
- JMH benchmark module — premature; the processor generates no runtime overhead today
- MethodHandle caching — there is no reflection in hot paths to replace
- YAML-driven type class configuration — over-engineered for this use case
- `ProcessorDebugger` CLI tool — not justified by current usage patterns
- Monad transformers / Bifunctor — valuable but deferred to post-2.0
- Fuzz testing, 24-hour stability runs — not proportionate
- Security audit with crypto-signed registrations — out of scope
- Community review cycle — deferred; no established user base yet

---

## Remaining Work (10 weeks, March 23 – May 31)

### Phase A: SQL Module Completion (Weeks 1–3, ends April 13)

The SQL module implementation is complete. One documentation task remains.

#### A.1 SQL module documentation

Update `docs/EXAMPLES.md` with a worked SQL example showing:
- Building a `Query` with `RowDecoder` primitives and combinators
- Applying `SqlAstCompiler.compile` to inspect the generated SQL
- The `QueryFunctor` operations (`map`, `join`, `disjunction`)

---

### Phase B: Annotation Processor — Code Generation (Weeks 3–6, ends May 4)

The processor currently **validates** that required methods are present but generates nothing. The original v2.0 goal of making annotation-driven code useful requires actually deriving the missing methods.

#### B.1 Code generation for `@Functor`

**What to generate:** `mapConst(F<A>, B): F<B>` derived from `map`.

```java
// User writes:
@Functor
class MyFunctor implements IFunctor<My<?>> {
    static <A,B> My<B> map(My<A> fa, Function<A,B> f) { … }
}

// Processor generates (into the same class or a companion):
static <A,B> My<A> mapConst(My<B> fb, A a) {
    return map(fb, ignored -> a);
}
```

This is the simplest possible derivation and serves as the proof-of-concept for code generation.

**Implementation notes:**
- Use `javax.annotation.processing.Filer` to write a source file (or inject into the class via byte-code — source file is simpler and more debuggable)
- Generate only if `mapConst` is not already present
- Follow existing `FunctionalCompiler` → `CompilerFactory` → `Compiler` structure; add a `CodeGenerator` step after validation passes

#### B.2 Code generation for `@Applicative`

Derive `keepLeft` and `keepRight` from `liftA2`.

```java
static <A,B> F<A> keepLeft(F<A> fa, F<B> fb)  { return liftA2((a,b) -> a, fa, fb); }
static <A,B> F<B> keepRight(F<A> fa, F<B> fb) { return liftA2((a,b) -> b, fa, fb); }
```

#### B.3 Code generation for `@Monad`

Derive `join` from `flatMap`, and `map`/`fapply` if not provided.

```java
static <A> F<A> join(F<F<A>> ffa) { return flatMap(Function.identity(), ffa); }
```

**Acceptance criteria for Phase B:**
- Generated methods are syntactically correct Java
- Generated code compiles when the processor runs on `example-functional`
- Existing validation tests still pass
- At least one test in `example-functional` verifies a generated method is callable

---

### Phase C: Test Coverage & Law Verification (Weeks 6–9, ends May 18)

#### C.1 Property-based law tests

Add [jqwik](https://jqwik.net/) to `example-functional` test scope. Write parameterised law checks for the existing built-in instances:

| Law                                      | Instances to verify             |
|------------------------------------------|---------------------------------|
| Functor identity/composition             | `FiniteList`, `Maybe`, `Either` |
| Monad left/right identity, associativity | `Maybe`, `Either`, `FiniteList` |
| Monoid associativity + identity          | `FiniteList` (as monoid)        |
| Semigroup associativity                  | integer/string semigroups       |

Format: abstract base `FunctorLawTest<F>` that sub-tests inherit; sub-test provides a generator and the type class instance.

#### C.2 Compiler test coverage

`functional-compiler` tests currently only cover happy paths via `ConcurrentTypeClassSanityTest` and the annotation processing sanity checks. Add:

- Missing required method → correct error message emitted
- Wrong method signature → correct error message
- Class annotated but interface not implemented → correct error message
- Multiple annotations on the same class (e.g., `@Functor @Monad`) → both validated

---

### Phase D: Documentation & Release Prep (Weeks 9–12, ends May 25)

#### D.1 Javadoc pass

Every public class and method in `annotation-definitions` needs a Javadoc comment explaining:
- What the structure/annotation requires
- Which methods are required vs. generated
- A one-line example

#### D.2 Update existing docs

- `docs/FUNCTIONAL_STRUCTURES.md` — add `@Alternative` specification; update `@Applicative` to mention `keepLeft`/`keepRight` generation
- `docs/EXAMPLES.md` — add SQL example (from A.2)
- `docs/PARSER.md` — review against current `ParserApplicative` implementation; fix any drift
- `CHANGELOG.md` — update with all changes on `feature/overhaul-lists`

#### D.3 API freeze

- Tag the public API surface: add `@SuppressWarnings("unused")` where needed; mark internal types with a comment
- Write a short `docs/API_STABILITY.md` that lists which packages are stable API vs. internal

---

### Phase E: Release (Weeks 12–16, ends May 31)

#### E.1 Merge and integration

- Merge `feature/overhaul-lists` into `master`
- Verify CI is green on Java 17, 21, 24
- Bump version to `2.0` in all POMs

#### E.2 Maven Central publication

- Build + GPG-sign all artifacts: `mvn deploy -Pgpg`
- Promote from Sonatype staging
- Verify installation with a fresh empty project

#### E.3 GitHub Release

- Tag `v2.0.0`
- Write release notes summarising the v1.3 → v2.0 changes
- Link to `docs/BREAKING_CHANGES.md` for migration

---

## Revised Exit Criteria for v2.0

| Criterion                                                  | Status    |
|------------------------------------------------------------|-----------|
| Java 17+ minimum, CI on 17/21/24                           | ✅ Done    |
| `SqlRunner` complete                                       | ✅ Done    |
| At least one annotation generates derived methods          | ⬜ Pending |
| All built-in instances verified against functor/monad laws | ⬜ Pending |
| Compiler error messages tested                             | ⬜ Pending |
| Javadoc on all public API symbols                          | ⬜ Pending |
| CHANGELOG up to date                                       | ⬜ Pending |
| CI green on master after merge                             | ⬜ Pending |
| Published to Maven Central                                 | ⬜ Pending |
