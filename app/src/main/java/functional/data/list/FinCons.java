package functional.data.list;

import java.util.function.Function;

final class FinCons<A> extends Cons<A> implements FiniteList<A> {

    FinCons(A head, FiniteList<A> tail){
        super(head, tail);
    }

    @Override
    public FiniteList<A> tail() {
        return (FiniteList<A>) super.tail();
    }

    @Override
    public <B> FiniteList<B> map(Function<A, B> mapping) {
        return FiniteList.super.map(mapping);
    }
}
