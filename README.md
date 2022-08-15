# Functional Java by Annotations

This projects tries to bring proper functional elements to Java 
through annotation processing.

### How to

To create use it you just need to implement ``IFuntor<List<?>>``
substituting ``IFunctor`` and ``List`` for the functional structure 
and the type constructor you would like to use.

In addition, you need to annotate the class that is implementing such a
structure with the correct annotation. In this case ``@Functor``.

As the last step, add the annotation processor ``functional.annotation.FunctionalCompiler``
to your project.

Compilation will fail until you add enough methods to satisfy the functional
structure requirements.

## Functionals
### Functor

A functor requires only one function
```java
public static <A,B> F<B> map(F<A> base, Function<A,B> map)
```
and it satisfies the following law:
```
map(x, id) == x
map(map(x, f), g) == map(x, f . g)
```

The miminal required is the only function it has: *map*

### Applicative

Any applicative is a functor, so it also has a map function added to the following:
````java
public static <A> F<A> pure(A a)
public static <A,B> F<B> fapply(F<Function<A,B>> map, F<A> base)
public static <A,B,C> F<C> liftA2(BiFunction<A,B,C> map, F<A> fa, F<B> fb)
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
public static <A,B> F<B> flatMap(Function<A,F<B>> map, F<A> base)
public static <A> F<A> join(F<F<A>> doubleMonad)
````
and it satisfies the following laws (adding the ones from applicative):
````
flatMap(id, ffa) == join(ffa)
flatMap(pure . f, base) == map(f, base)
fapply(f, base) == flatMap(g -> map(g, base)), f)
join(pure(fa)) == fa
````

The minimal required is *pure* and one of the following lists:
    1. *flatMap*
    2. *join* and the minimal Functor
    3. *join* and the minimal Applicative