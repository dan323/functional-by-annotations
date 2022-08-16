package annotation.functor;

import functional.annotation.Functor;
import functional.annotation.iface.IFunctor;

import java.util.List;
import java.util.function.Function;

@Functor
public class FunctorMock<A> implements IFunctor<List<?>> {

    private final List<A> lst;

    public FunctorMock(List<A> lst){
        this.lst = lst;
    }

    public static <T,R> List<R> map(List<T> base, Function<T,R> mapping){
        return base.stream().map(mapping).toList();
    }

    public List<A> toList(){
        return lst;
    }
}
