package com.dan323.functional.data.either;

import com.dan323.functional.data.Void;

import java.util.function.Function;

public sealed interface Either<A, B> permits Left, Right {

    <C> C either(Function<A, C> aToC, Function<B, C> bToC);

    static <A, B> Either<A, B> left(A a) {
        return new Left<>(a);
    }

    static <A, B> Either<A, B> right(B b) {
        return new Right<>(b);
    }

    static <A> A leftVoid(Either<Void, A> either) {
        return either.either(Void::absurd, Function.identity());
    }

    static <A> A rightVoid(Either<A, Void> either) {
        return either.either(Function.identity(), Void::absurd);
    }
}
