package functional.data.list;

import functional.data.optional.Optional;

public final class Nil<A> implements List<A> {

    private Nil() {
    }

    static Nil<?> NIL = new Nil<>();

    @Override
    public Optional<A> head() {
        return Optional.of();
    }

    @Override
    public List<A> tail() {
        return new Nil<>();
    }

    @Override
    public String toString() {
        return "[]";
    }
/*
    @Override
    public <R> List<R> map(Function<A, R> f) {
        return (List<R>) NIL;
    }

    @Override
    public <R> List<R> flatMap(Function<A, Monad<R, List<?>>> f) {
        return (List<R>) NIL;
    }

    @Override
    public <Q> List<Q> fapply(Applicative<Function<A, Q>, List<?>> ff) {
        return List.nil();
    }*/

    @Override
    public boolean equals(Object obj) {
        return obj == NIL;
    }
}
