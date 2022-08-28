package com.dan323.functional.data.list.zipper;

import com.dan323.functional.annotation.Functor;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.data.list.FiniteListFunctional;

import java.util.List;
import java.util.function.Function;

@Functor
public class ListZipperFunctor implements IFunctor<ListZipper<?>> {

    private static final ListZipperFunctor LIST_ZIPPER = new ListZipperFunctor();

    public static <A, B> ListZipper<B> map(ListZipper<A> base, Function<A, B> mapping) {
        return new ListZipper<>(FiniteListFunctional.map(base.getLeft(), mapping), mapping.apply(base.get()), FiniteListFunctional.map(base.getRight(), mapping));
    }

    public static ListZipperFunctor getInstance() {
        return LIST_ZIPPER;
    }
}
