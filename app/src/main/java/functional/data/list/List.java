package functional.data.list;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;
import functional.data.optional.Maybe;

import java.util.function.Function;
import java.util.function.UnaryOperator;

@Functor
public sealed interface List<A> extends IFunctor<List<?>> permits Cons, FiniteList, Generating, Generating.GeneratingMapped, Repeat {

    Maybe<A> head();

    List<A> tail();

    <B> List<B> map(Function<A,B> mapping);

    default FiniteList<A> limit(int k){
        return head().maybe(h -> FiniteList.cons(h, tail().limit(k-1)), FiniteList.nil());
    }

    static <A> List<A> generate(A first, UnaryOperator<A> generator){
        return new Generating<>(first, generator);
    }

    static <A> List<A> cons(A first, List<A> tail) {
        if (first == null || tail == null) {
            throw new IllegalArgumentException("No input can be null");
        }
        return new Cons<>(first, tail);
    }

    static <A> List<A> nil() {
        return (List<A>) Nil.NIL;
    }

    static <A,B> List<B> map(List<A> lst, Function<A,B> mapping){
        return lst.map(mapping);
    }

    static <A> List<A> repeat(A a){
        return new Repeat<>(a);
    }
}
/*
    public <Q> List<Q> fapply(Applicative<Function<A, Q>, List<?>> ff);

    default <Q> List<Q> pure(Q a){
        return List.cons(a, List.nil());
    }

    @Override
    default <R> List<R> join(Monad<Monad<R,List<?>>,List<?>> lst){
        return ListUtils.join((List<List<R>>) (Monad<?,?>) lst);
    }
}
*/