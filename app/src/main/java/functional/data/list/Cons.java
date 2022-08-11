package functional.data.list;
import functional.data.optional.Maybe;

import java.util.Objects;
import java.util.function.Function;

/**
 * List of one element in front of another list
 * @param <A>
 */
sealed class Cons<A> implements List<A> permits FinCons {

    private final A head;
    private final List<A> tail;

    Cons(A head, List<A> tail) {
        if (head == null || tail == null){
            throw new IllegalArgumentException();
        }
        this.head = head;
        this.tail = tail;
    }

    @Override
    public Maybe<A> head() {
        return Maybe.of(head);
    }

    @Override
    public List<A> tail() {
        return tail;
    }

    @Override
    public <B> List<B> map(Function<A, B> mapping) {
        return new Cons<>(mapping.apply(head), tail().map(mapping));
    }

    @Override
    public String toString(){
        return "[" + head + "," + tail + "]";
    }
/*
    @Override
    public <R> List<R> flatMap(Function<A, Monad<R,List<?>>> f) {
        return ListUtils.join((List<List<R>>) (List<?>)map(f));
    }
*/
    @Override
    public boolean equals(Object obj){
        if (obj == this) return true;
        if (obj == null || obj.getClass() != getClass()) return false;
        Cons<?> k = ((Cons<?>)obj);
        return Objects.equals(k.head, head) && Objects.equals(k.tail, tail);
    }

    @Override
    public int hashCode(){
        return Objects.hash(head, tail);
    }
/*
    @Override
    public <Q> List<Q> fapply(Applicative<Function<A, Q>, List<?>> ff) {
        var fg = (List<Function<A,Q>>) ff;
        return fg.head().maybe(h -> ListUtils.concat(this.map(h), fapply(fg.tail())), List.nil());
    }*/
}