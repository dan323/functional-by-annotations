package com.dan323.functional.data.parser;

import com.dan323.functional.annotation.Alternative;
import com.dan323.functional.annotation.Applicative;
import com.dan323.functional.annotation.funcs.IAlternative;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.state.StateMonad;

import java.util.function.Function;
import java.util.function.Supplier;

@Applicative
@Alternative
public class ParserApplicative implements IApplicative<Parser<?>>, IAlternative<Parser<?>> {

    private static final StateMonad<String, FiniteList<Parser.ParserError>> STATE_MONAD = StateMonad.getInstance();

    private static final ParserApplicative PARSER_MONAD = new ParserApplicative();

    public static ParserApplicative getInstance() {
        return PARSER_MONAD;
    }

    @Override
    public Class<Parser<?>> getClassAtRuntime() {
        return (Class<Parser<?>>) (Class<?>) Parser.class;
    }

    public static <A, B> Parser<B> map(Parser<A> p, Function<A, B> function) {
        return STATE_MONAD.map(p, function)::apply;
    }

    public static <A> Parser<A> pure(A a) {
        return STATE_MONAD.pure(a)::apply;
    }

    public static <A, B> Parser<B> fapply(Parser<Function<A, B>> fun, Parser<A> st) {
        return STATE_MONAD.fapply(fun, st)::apply;
    }

    public static <A> Parser<A> empty() {
        return _ -> Either.left(FiniteList.of(Parser.ParserError.empty()));
    }

    public static <A> Parser<A> disjunction(Parser<A> first, Parser<A> second) {
        return s -> first.apply(s).either(_ -> second.apply(s), Either::right);
    }

    public static <A> Parser<FiniteList<A>> many(Parser<A> parser) {
        return disjunction(some(parser), pure(FiniteList.nil()));
    }

    public static <A, B> Parser<B> whenFailureWhenSuccess(Parser<A> parser, Supplier<Parser<B>> errorParser, Supplier<Parser<B>> successParser) {
        return s -> parser.apply(s).either(_ -> errorParser.get().apply(s), _ -> successParser.get().apply(s));
    }

    public static <A> Parser<FiniteList<A>> some(Parser<A> parser) {
        return whenFailureWhenSuccess(parser, () -> map(parser, FiniteList::of), () -> fapply(map(parser, a -> (lst -> FiniteList.cons(a, lst))), many(parser)));
    }
}
