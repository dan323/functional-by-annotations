package functional.data.list;
import functional.data.optional.Optional;

import java.util.Objects;

public final class Cons<A> implements List<A> {

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
    public Optional<A> head() {
        return Optional.of(head);
    }

    @Override
    public List<A> tail() {
        return tail;
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