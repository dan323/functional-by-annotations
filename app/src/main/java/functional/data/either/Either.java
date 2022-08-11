package functional.data.either;

import java.util.function.Function;

public sealed interface Either<A,B> permits Left, Right {
    
    <C> C either(Function<A,C> aToC, Function<B,C> bToC);

    static <A,B> Either<A,B> left(A a){
        return new Left<>(a);
    }

    static <A,B> Either<A,B> right(B b){
        return new Right<>(b);
    }

}
