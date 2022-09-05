package com.dan323.functional.data.list.zipper;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.optional.MaybeMonad;

import java.util.function.Function;

@Functor
public final class ListZipperFunctor implements IFunctor<ListZipper<?>> {

    private static final ListZipperFunctor LIST_ZIPPER = new ListZipperFunctor();

    public static <A, B> ListZipper<B> map(ListZipper<A> base, Function<A, B> mapping) {
        Maybe<B> ma = MaybeMonad.map(base.get(), mapping);
        FiniteList<B> bList = ma.maybe(p -> FiniteList.cons(p, FiniteListFunctional.map(base.getRight(), mapping)), FiniteList.nil());
        return new ListZipper<>(FiniteListFunctional.map(base.getLeft(), mapping), bList);

    }

    public static ListZipperFunctor getInstance() {
        return LIST_ZIPPER;
    }

    @Override
    public Class<ListZipper<?>> getClassAtRuntime() {
        return (Class<ListZipper<?>>) (Class) ListZipper.class;
    }
}
