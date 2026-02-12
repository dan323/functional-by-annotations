# Algebraic Structures

Algebraic structures operate directly on types like `Integer`, `Boolean`.

## Semigroup

**Purpose**: Associative binary operation

**Required Methods**: `op`

**Law**: Associativity: `op(op(a, b), c) == op(a, op(b, c))`

**Example**:
```java
@Semigroup
public class AndSemigroup implements ISemigroup<Boolean> {
    public static Boolean op(Boolean a, Boolean b) {
        return a && b;
    }
}
```

## Monoid

**Purpose**: Semigroup with identity element

**Required Methods**: `op` + `unit`

**Laws**:
1. Left identity: `op(unit(), a) == a`
2. Right identity: `op(a, unit()) == a`
3. Associativity: inherited from Semigroup

**Example**:
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

## Ring

**Purpose**: Structure with addition and multiplication

**Example**:
```java
@Ring
public class IntegerRing implements IRing<Integer> {
    // Addition (forms a commutative group)
    public static Integer add(Integer a, Integer b) {
        return a + b;
    }
    
    public static Integer zero() {
        return 0;
    }
    
    public static Integer negate(Integer a) {
        return -a;
    }
    
    // Multiplication (associative with identity)
    public static Integer multiply(Integer a, Integer b) {
        return a * b;
    }
    
    public static Integer one() {
        return 1;
    }
}
```

---

**See also**: [Functional Structures](FUNCTIONAL_STRUCTURES.md) | [Examples](EXAMPLES.md)

