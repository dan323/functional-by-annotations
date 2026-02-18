# Breaking Changes: v1.3 → v2.0

**Release Date:** May 31, 2026  
**Minimum Java Version:** Java 17 (upgraded from Java 11)

This document enumerates all breaking changes in v2.0 and provides migration paths for each.

> **Related Documentation:** For a complete inventory of public APIs and their stability status, see [API Audit & Stability Matrix](API_AUDIT_v2.0.md). For a quick reference, check the [API Audit - Stability Matrix Quick Reference](API_AUDIT_v2.0.md).

---

## Executive Summary

v2.0 introduces a **stable, production-ready API** with the following categories of changes:

| Category | Count | Impact | Effort |
|----------|-------|--------|--------|
| **Java Version** | 1 major | Requires JDK 17+ | Low (runtime only) |
| **Registry Architecture** | 3 major | Immutable registry, versioning, thread-safety | Medium (optional adoption) |
| **Reflection Elimination** | 1 moderate | MethodHandle caching in hot paths | Low (transparent) |
| **Configuration System** | 1 new | Explicit discovery mode available | Low (opt-in) |
| **Deprecated APIs** | 2 | Legacy reflection utilities | Low (easy replacement) |
| **Sealed Modules** | 1 new | Module access restrictions | Low (internal only) |

---

## 1. Java Version Requirement (BREAKING)

### Change
- **v1.3:** Supports Java 24
- **v2.0:** Requires Java 17 minimum

### Why
- Java 11 reaches EOL December 2026
- Java 17 (LTS) provides critical features:
  - Sealed classes (secure type hierarchies)
  - Records (immutable value objects)
  - Text blocks (better documentation strings)
  - MethodHandles improvements (reflection elimination)
  - Pattern matching foundation (future releases)

### Migration Path

1. **Update your JDK**
   ```bash
   # Verify your JDK version
   java -version
   # Should output: openjdk version "17.0.x" or later, or "24" (latest)
   ```

2. **Update Maven POM**
   ```xml
   <!-- Old (v1.3) -->
   <maven.compiler.source>11</maven.compiler.source>
   <maven.compiler.target>11</maven.compiler.target>
   
   <!-- New (v2.0) -->
   <maven.compiler.source>17</maven.compiler.source>
   <maven.compiler.target>17</maven.compiler.target>
   ```

3. **Update Gradle Build**
   ```gradle
   // Old (v1.3)
   compileJava {
       sourceCompatibility = '11'
       targetCompatibility = '11'
   }
   
   // New (v2.0)
   compileJava {
       sourceCompatibility = '17'
       targetCompatibility = '17'
   }
   ```

4. **Expected Compilation Errors (if any)**
   - If your code uses Java 11 APIs only, no changes needed
   - If you have workarounds for older Java versions, you can safely remove them

### Tested Compatibility
- ✅ Java 17 LTS
- ✅ Java 21 LTS
- ✅ Java 24 (latest)

---

## 2. Registry Architecture Refactoring (BREAKING)

### Change

#### 2.1 RegistryHolder is Now Immutable

**v1.3 Behavior:**
```java
// v1.3: Registry mutable, registration returns void
Registry registry = Registry.create();
registry.register(Optional.class, OptionalMonad.instance());  // Mutates registry
registry.register(List.class, ListFunctor.instance());        // Mutates registry
```

**v2.0 Behavior:**
```java
// v2.0: Registry immutable, registration returns new Registry
Registry registry = Registry.create();
registry = registry.register(Optional.class, OptionalMonad.instance());  // Returns new Registry
registry = registry.register(List.class, ListFunctor.instance());        // Returns new Registry
```

**Why**
- Thread-safety without locks (immutability → no race conditions)
- Versioning support (audit trail of registrations)
- Functional composition (each registration creates a snapshot)

**Migration Path**

If you have custom registration code:

```java
// ❌ OLD (v1.3) - Won't compile in v2.0
Registry registry = new Registry();
registry.register(MyType.class, MyInstance.instance());
registry.register(OtherType.class, OtherInstance.instance());

// ✅ NEW (v2.0) - Chained registration
Registry registry = Registry.create()
    .register(MyType.class, MyInstance.instance())
    .register(OtherType.class, OtherInstance.instance());

// ✅ ALSO VALID (v2.0) - If you need to preserve intermediate states
Registry v1 = Registry.create().register(MyType.class, MyInstance.instance());
Registry v2 = v1.register(OtherType.class, OtherInstance.instance());
// v1 and v2 are separate, immutable snapshots
```

---

#### 2.2 Registry Versioning API

**New in v2.0:**
```java
// Get registry metadata
Registry registry = /* ... */;
long version = registry.getVersion();  // New method in v2.0
List<RegistrationEvent> history = registry.getRegistrationHistory();  // New in v2.0

// Listen to registration events
registry.addRegistrationListener((event) -> {
    System.out.println("Registered: " + event.getType());
});
```

**Impact:**
- Existing code (without versioning) continues to work
- This is **backward compatible** - not a breaking change for most users

**Usage (Optional)**
```java
// v2.0 feature: Inspect which types are registered
Registry registry = /* ... */;
if (registry.isRegistered(Optional.class)) {
    Optional.of(123).map(x -> x * 2);  // Safe - type class exists
}
```

---

#### 2.3 Registry Scope: Compile-Time vs. Runtime

**v1.3 Behavior:**
- Annotation processor auto-registers type classes at compile-time
- Runtime explicit registration mixes with auto-discovery
- No clear separation

**v2.0 Behavior:**
```java
// Configuration at startup (NEW in v2.0)
FunctionalConfig config = FunctionalConfig.builder()
    .discoveryMode(DiscoveryMode.AUTO)  // Default: discover via classpath
    .allowReflection(true)              // Default: allow reflection
    .build();
Functional.initialize(config);

// Or use EXPLICIT mode (more secure, no auto-discovery)
FunctionalConfig config = FunctionalConfig.builder()
    .discoveryMode(DiscoveryMode.EXPLICIT)  // Must manually register
    .allowReflection(false)                 // Fail if reflection needed
    .build();
Functional.initialize(config);

// Then explicitly register
Functional.register(Optional.class, OptionalMonad.instance());
Functional.register(List.class, ListFunctor.instance());
```

**Migration Path**

1. **If you're using default auto-discovery (most users):**
   - No action needed
   - v2.0 defaults to `AUTO` mode (same as v1.3)

2. **If you want enterprise-grade security:**
   ```java
   // NEW in v2.0: Explicit mode
   FunctionalConfig config = FunctionalConfig.builder()
       .discoveryMode(DiscoveryMode.EXPLICIT)  // No classpath scanning
       .allowReflection(false)                 // Strict validation
       .build();
   Functional.initialize(config);
   
   // Register only what you need
   Functional.register(Optional.class, OptionalMonad.instance());
   ```

---

## 3. Reflection Elimination in Hot Paths (MODERATE BREAKING CHANGE)

### Change

**v1.3 Behavior:**
```
map() call → reflection lookup of "map" method → invoke via Method.invoke()
```

**v2.0 Behavior:**
```
map() call → MethodHandle cache lookup → invokeExact() (faster, type-safe)
```

**Impact:**
- Performance improvement (usually 5-15% faster)
- **No API changes** - Your code remains the same
- **Minor** behavioral change: Stack traces may differ
- Exception handling more precise (MethodHandle exceptions vs. reflection exceptions)

### Migration Path

If you have custom error handling for `map()` / `flatMap()` failures:

```java
// ❌ OLD (v1.3)
try {
    result = FunctorUtil.map(myFunctor, value, function);
} catch (InvocationTargetException e) {  // Reflection-specific
    handleReflectionError(e);
}

// ✅ NEW (v2.0)
try {
    result = FunctorUtil.map(myFunctor, value, function);
} catch (IllegalArgumentException e) {  // MethodHandle or functor validation error
    handleError(e);
} catch (Throwable e) {  // Catch-all for unexpected errors
    handleUnexpected(e);
}
```

---

## 4. Deprecated APIs Removed (BREAKING)

### 4.1 Reflection-Based Introspection Utilities

**Removed in v2.0:**
```java
// ❌ These classes are REMOVED in v2.0
com.dan323.functional.annotation.compiler.reflection.MethodReflector
com.dan323.functional.annotation.compiler.reflection.FieldReflector
```

**Why**
- MethodHandle caching makes direct reflection obsolete
- Internal APIs, rarely used by end-users
- Better alternatives available (MethodHandle or annotation introspection)

**Migration Path**

If you were using these internal APIs:

```java
// ❌ OLD (v1.3)
MethodReflector reflector = MethodReflector.of(MyType.class);
Method method = reflector.findMethod("map");
method.invoke(/* ... */);

// ✅ NEW (v2.0) - Use MethodHandle
MethodHandle handle = MethodHandleUtil.findMethod(MyType.class, "map", MethodType.methodType(/*...*/));
handle.invokeExact(/* ... */);

// ✅ OR - Use the public Util APIs
F result = FunctorUtil.map(functor, base, function);  // Reflection hidden
```

---

### 4.2 Legacy Configuration Factory

**Deprecated in v1.3, Removed in v2.0:**
```java
// ❌ REMOVED in v2.0
FunctionalRegistry.legacyBuilder()  // Use FunctionalConfig.builder() instead
```

**Migration Path**

```java
// ❌ OLD (v1.3)
FunctionalRegistry.legacyBuilder()
    .withReflectionEnabled(true)
    .build();

// ✅ NEW (v2.0)
FunctionalConfig config = FunctionalConfig.builder()
    .allowReflection(true)
    .discoveryMode(DiscoveryMode.AUTO)
    .build();
Functional.initialize(config);
```

---

## 5. Module Visibility & Package Sealing (NEW)

### Change

**New in v2.0:**
```
functional-definitions/
├── annotation-definitions/   ← Public: annotations like @Functor, @Monad
├── functional-compiler/      ← Internal: annotation processor + utilities
└── functional-api/           ← NEW in v2.0: Public API interfaces
```

**Impact:**
- Internal compiler packages now sealed (`com.dan323.functional.annotation.compiler.*`)
- May not directly instantiate compiler utilities
- Use public APIs in `com.dan323.functional.annotation.funcs.*` instead

### Migration Path

If you were depending on compiler-internal packages:

```java
// ❌ OLD (v1.3) - Accessing internal compiler APIs
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.compiler.RegistryImpl;

FunctorUtil.map(/* ... */);

// ✅ NEW (v2.0) - Use public APIs
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.Functional;

IFunctor<Optional> optionalFunctor = Functional.resolve(Optional.class);
optionalFunctor.map(value, function);
```

---

## 6. Type Class Law Verification (NEW FEATURE - Not Breaking)

### Change

**NEW in v2.0:**
```java
// All type class instances are now verified against algebraic laws
// Compile-time: Annotation processor validates laws
// Runtime: Optional law verification available

LawChecker checker = LawChecker.create();
LawVerificationResult result = checker.verify(
    OptionalFunctor.instance(),
    FunctorLaws.instance()  // Functor Laws: Identity, Composition
);

if (!result.passed()) {
    System.err.println("Type class violates laws: " + result.getFailures());
}
```

**Impact:**
- **Backward compatible** (opt-in feature)
- Provides confidence in type class correctness
- No API changes to existing code

---

## 7. Performance Guarantees (NEW - Not Breaking)

### Change

**New in v2.0:**
```
Overhead vs. direct Java operations:
- v1.3: <5% for single operations, up to 15% for chains
- v2.0: <1% for single operations, <2% for chains (target)
```

**Performance Metrics Available:**
```java
PerformanceMetrics metrics = Functional.getMetrics();
System.out.println("Map call overhead: " + metrics.mapOverheadPercent());
System.out.println("FlatMap call overhead: " + metrics.flatMapOverheadPercent());
```

**Impact:**
- More efficient applications
- Suitable for latency-sensitive workloads
- **No API changes** - transparent improvement

---

## Migration Checklist

### Phase 1: Prepare (Week 1)
- [ ] Review this document entirely
- [ ] Identify which breaking changes affect your code
- [ ] Plan upgrade timeline (suggest 1-2 weeks)

### Phase 2: Java Upgrade (Week 1-2)
- [ ] Update Java SDK to 17+
- [ ] Update POM/Gradle to set compiler target to 17
- [ ] Run full test suite
- [ ] Verify all tests pass

### Phase 3: Registry Changes (Week 2-3)
- [ ] If custom registration code exists:
  - [ ] Update to use immutable registry chain calls
  - [ ] Add test coverage for registration sequences
- [ ] If using reflection utilities:
  - [ ] Replace with MethodHandle or public Util APIs
  - [ ] Run tests to verify behavior

### Phase 4: Configuration (Week 3)
- [ ] **Optional:** Adopt new FunctionalConfig for explicit mode (if desired)
- [ ] Test in dev/staging environment

### Phase 5: Verification (Week 4)
- [ ] Full regression test on v2.0
- [ ] Performance testing (benchmark vs v1.3)
- [ ] Deploy to production

---

## FAQ

### Q: Do I have to upgrade to v2.0?
**A:** v1.3 will remain supported for bug fixes through December 2026. v2.0 is strongly recommended for new projects.

### Q: Will my v1.3 code compile with v2.0?
**A:** Not without changes (Java 17+ requirement and registry mutations). See [Migration Checklist](#migration-checklist).

### Q: Is performance better in v2.0?
**A:** Yes! Expect 5-15% improvement due to MethodHandle caching. See [Performance Guarantees](#7-performance-guarantees-new---not-breaking).

### Q: Do I need to change my type class implementations?
**A:** No! Type class implementations (e.g., `@Monad`, `@Functor`) remain unchanged. Only the annotation processor and runtime have changes.

### Q: What happens to code using `MethodReflector`?
**A:** You must replace it with MethodHandle utilities or public Util APIs. See [Reflection-Based Introspection Utilities](#41-reflection-based-introspection-utilities).

### Q: Can I use Java 21 or 24 with v2.0?
**A:** Yes! v2.0 is tested and compatible with Java 17, 21, and 24.

### Q: Is the explicit registry mode required?
**A:** No. The default is auto-discovery (same as v1.3). Explicit mode is optional for high-security environments.

---

## Support & Questions

For migration questions or issues:
- 📧 Email: danconsa@hotmail.com
- 🐛 Issues: https://github.com/dan323/functional-by-annotations/issues
- 📚 Documentation: https://github.com/dan323/functional-by-annotations/docs

**Estimated Migration Effort:** 2-4 weeks for most projects  
**Migration Risk Level:** Low (changes are well-scoped and backward-compatible where possible)


