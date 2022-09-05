# Functional Java by Annotations

This projects tries to bring proper functional elements to Java
through annotation processing.

### How to

You need to annotate the class that is implementing such a
structure with the correct annotation. For example ``@Functor`` to implement ``IFunctor``.

#### Functionals

A functional structure is a set of operations defined over a type constructor or set of type constructors

To create use it you just need to implement the needed interface, like ``IFuntor<List<?>>``
substituting ``IFunctor`` and ``List`` for the functional structure 
and the type constructor you would like to use. The type constructor needs to use
the wildcard ``?``. Note that we may use type constructors with several inputs like
``Either<A,?>`` or ``Either<?,?>``.

It is not possible to concatenate constructors as in ``Maybe<List<?>>``

#### Algebraic Structures

An algebraic structure is a set of operations defined over a type or set of types. They
are type constructors, but they still require the corresponding annotation.

#### Compile time

As the last step, add the annotation processor ``com.dan323.functional.annotation.FunctionalCompiler``
to your project.

Compilation will fail until you add enough methods to satisfy the functional and algebraic
structure requirements.

## Algebraic Structures

An algebraic structure is a set of operations defined over a type or set of types.
Methods of super-types also count towards the annotation being implemented.

### Semigroup

A semigroup requires only one operation
````java
public T op(T a, T b);
````
and it should satisfy the associative property:
````
op(x,op(y,z)) == op(op(x,y),z)
````

## Functionals


### Functor

A functor requires only one function
```java
public <A,B> F<B> map(F<A> base, Function<A,B> map);
```
and it satisfies the following laws:
```
map(x, id) == x
map(map(x, f), g) == map(x, g . f)
```

The miminal required is the only function it has: *map*

### Applicative

Any applicative is a functor, so it also has a map function added to the following:
````java
public <A> F<A> pure(A a);
public <A,B> F<B> fapply(F<Function<A,B>> map, F<A> base);
public <A,B,C> F<C> liftA2(BiFunction<A,B,C> map, F<A> fa, F<B> fb);
````
and it satisfies the following laws (adding the ones from functor):
````
fapply(pure(f), base) == map(base, f)
map(g, pure(x)) == pure(g(x))
fapply(u, pure(y)) == fapply(pure(f -> f(y)), u)
liftA2(f, a, b) == fapply(map(f, a), b)
fapply(f,base) == liftA2((a,b) -> a(b), f, base)
````

The mimimal required is *pure* and, either *fapply* or *liftA2*.

### Monad

Any monad is an applicative, and hence a functor, so it has all their functions plus the following:
````java
public <A,B> F<B> flatMap(Function<A,F<B>> map, F<A> base);
public <A> F<A> join(F<F<A>> doubleMonad);
````
and it satisfies the following laws (adding the ones from applicative):
````
flatMap(id, ffa) == join(ffa)
flatMap(pure . f, base) == map(f, base)
fapply(f, base) == flatMap(g -> map(g, base), f)
join(pure(fa)) == fa
````

The minimal required is *pure* and one of the following lists:
1. *flatMap*
2. *join* and the minimal Functor
3. *join* and the minimal Applicative
