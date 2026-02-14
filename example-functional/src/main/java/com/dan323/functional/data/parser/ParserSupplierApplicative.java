package com.dan323.functional.data.parser;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.pair.Pair;

import java.util.function.Function;
import java.util.function.Supplier;

@Applicative
@Alternative
public final class ParserSupplierApplicative implements IApplicative<Supplier<Parser<?>>>, IAlternative<Supplier<Parser<?>>> {

    private ParserSupplierApplicative() {
    }

    private static final ParserSupplierApplicative PARSER_SUPPLIER_APPLICATIVE = new ParserSupplierApplicative();

    public static ParserSupplierApplicative getInstance() {
        return PARSER_SUPPLIER_APPLICATIVE;
    }

    @Override
    public Class<Supplier<Parser<?>>> getClassAtRuntime() {
        return (Class<Supplier<Parser<?>>>) (Class<?>) Supplier.class;
    }

    public static <A, B> Supplier<Parser<B>> map(Supplier<Parser<A>> p, Function<A, B> function) {
        return () -> ParserApplicative.map(p.get(), function);
    }


    public static <A> Supplier<Parser<A>> pure(A a) {
        return () -> ParserApplicative.pure(a);
    }

    public static <A, B> Supplier<Parser<B>> fapply(Supplier<Parser<Function<A, B>>> fun, Supplier<Parser<A>> st) {
        return () -> s -> fun
                .get()
                .apply(s)
                .either(
                        Either::left,
                        p1 -> st.get().apply(p1.getValue()).either(Either::left, p2 -> Either.right(new Pair<>(p1.getKey().apply(p2.getKey()), p2.getValue())))
                );
    }

    public static <A> Supplier<Parser<A>> empty() {
        return () -> s -> Either.left(FiniteList.of(Parser.ParserError.empty()));
    }

    public static <A> Supplier<Parser<A>> disjunction(Supplier<Parser<A>> first, Supplier<Parser<A>> second) {
        return () -> s -> first.get().apply(s).either(_ -> second.get().apply(s), Either::right);
    }

}
