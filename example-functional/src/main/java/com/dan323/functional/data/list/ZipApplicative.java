package com.dan323.functional.data.list;

import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IApplicative;

import java.util.function.BiFunction;
import java.util.function.Function;

@Applicative
public final class ZipApplicative implements IApplicative<List<?>> {

    public static <A> List<A> pure(A a) {
        return List.repeat(a);
    }

    /**
     * Depending on the type of the lists, we can have different implementations of liftA2. If one of the lists is finite, we can stop when we reach the end of it. If one of the lists is infinite, we can stop when we reach the end of the other list. If both lists are infinite, we can zip them together.
     * <br>
     * If one of the lists is finite, we apply the bifunction sequentially until the shortest list is empty.
     * <br>
     * If one of the lists is constantly repeating an element, we can create a mapped list by fixing
     * in the bifunction the element of the repeating list and applying it to the other list.
     *
     * @param fun bifunction to apply to the elements of the lists
     * @param lstA first list
     * @param lstB second list
     * @return a list containing the results of applying the bifunction to the elements of the lists
     * @param <A> type of elements in the first list
     * @param <B> type of elements in the second list
     * @param <C> type of elements in the resulting list
     */
    public static <A, B, C> List<C> liftA2(BiFunction<A, B, C> fun, List<A> lstA, List<B> lstB) {
        if (lstA instanceof FiniteList<A>) {
            return lstA.head().maybe(a -> lstB.head().maybe(b -> FiniteList.cons(fun.apply(a, b), (FiniteList<C>) liftA2(fun, lstA.tail(), lstB.tail())), FiniteList.nil()), FiniteList.nil());
        } else if (lstB instanceof FiniteList<B>) {
            return lstB.head().maybe(a -> lstA.head().maybe(b -> FiniteList.cons(fun.apply(b, a), (FiniteList<C>) liftA2(fun, lstA.tail(), lstB.tail())), FiniteList.nil()), FiniteList.nil());
        } else if (lstA instanceof Repeat<A> ra) {
            Function<B,C> auxFun = b -> fun.apply(ra.getHead(), b);
            return lstB.map(auxFun);
        } else if (lstB instanceof Repeat<B> rb) {
            Function<A,C> auxFun = b -> fun.apply(b, rb.getHead());
            return lstA.map(auxFun);
        } else if (lstA instanceof Cons<A> ca) {
            return ca.zipBy(fun, (InfiniteList<B>) lstB);
        } else if (lstB instanceof Cons<B> cb) {
            return cb.zipBy((a,b) -> fun.apply(b,a), (InfiniteList<A>) lstA);
        } else {
            return new Zipped<>((InfiniteList<A>) lstA, fun, (InfiniteList<B>) lstB);
        }
    }

    public static <A, B> List<B> map(List<A> base, Function<A, B> map) {
        return base.map(map);
    }

    @Override
    public Class<List<?>> getClassAtRuntime() {
        return (Class<List<?>>) (Class<?>) List.class;
    }
}
