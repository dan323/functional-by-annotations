package functional.data.list;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;
import functional.data.optional.Optional;

import java.util.function.Function;

@Functor
public sealed interface List<A> extends IFunctor<A,List<?>> permits Nil, Cons {

    Optional<A> head();

    List<A> tail();

    static <A> List<A> cons(A first, List<A> tail) {
        if (first == null || tail == null) {
            throw new IllegalArgumentException("No input can be null");
        }
        return new Cons<>(first, tail);
    }

    static <A> List<A> nil() {
        return (List<A>) Nil.NIL;
    }

    static <A> List<A> of(A... a) {
        if (a.length == 0) {
            return nil();
        } else {
            return List.of(1, a);
        }
    }

    private static <A> List<A> of(int n, A... a) {
        if (n >= a.length) {
            return nil();
        } else {
            return cons(a[n], of(n + 1, a));
        }
    }

    static <A,B> List<B> map(List<A> lst, Function<A,B> mapping){
        return lst.head().maybe(h -> List.cons(mapping.apply(h), map(lst.tail(),mapping)), nil());
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