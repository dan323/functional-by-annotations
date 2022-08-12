package functional.data.list;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public class JListFunctor implements IFunctor<List<?>> {

    public static <A,B> List<B> map(List<A> base, Function<A,B> mapping){
        return base.stream().map(mapping).toList();
    }
}
