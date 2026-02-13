package com.dan323.functional.data.parser;

import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.TraversalUtil;
import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.FiniteListFunctional;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.state.StateWithError;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.dan323.functional.data.list.FiniteList.cons;
import static com.dan323.functional.data.parser.Parser.ParserError.unexpectedEnd;

public interface Parser<A> extends StateWithError<A, String, FiniteList<Parser.ParserError>> {

    class ParserError {

        private final String message;

        private ParserError(String message) {
            this.message = message;
        }

        public static ParserError unexpectedChar(Character c) {
            return new ParserError("Unexpected character " + c);
        }

        public static ParserError unexpectedEnd() {
            return new ParserError("Unexpected empty string");
        }

        public static ParserError empty() {
            return new ParserError("This parser is empty");
        }

        @Override
        public String toString() {
            return "[ERROR]: " + message;
        }

        @Override
        public boolean equals(Object obj) {
            return (obj instanceof Parser.ParserError parserError) && Objects.equals(parserError.message, message);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(message);
        }
    }


    static Parser<Character> predicateParser(Predicate<Character> predicate) {
        return s -> {
            if (s.isEmpty()) {
                return Either.left(FiniteList.of(unexpectedEnd()));
            } else {
                return predicate.test(s.charAt(0)) ? Either.right(new Pair<>(s.charAt(0), s.substring(1))) : Either.left(FiniteList.of(ParserError.unexpectedChar(s.charAt(0))));
            }
        };
    }

    static Parser<Character> charParser(Character c) {
        return predicateParser(ch -> c != null && c.equals(ch));
    }

    static Parser<String> stringParser(String st) {
        if (st.isEmpty()) {
            return s -> Either.right(new Pair<>("", s));
        } else {
            Parser<FiniteList<Character>> parserList = (Parser<FiniteList<Character>>) TraversalUtil.traverse(
                    FiniteListFunctional.getInstance(),
                    ParserApplicative.getInstance(),
                    (Function<Character, Parser<Character>>) Parser::charParser,
                    FiniteList.fromJavaList(st.chars().mapToObj(c -> (char) c).toList()));
            return ParserApplicative.map(parserList, Parser::fromChars);
        }
    }

    static Parser<Integer> intParser() {
        return ParserApplicative.map(ParserApplicative.some(predicateParser(Character::isDigit)), ((Function<FiniteList<Character>, String>) Parser::fromChars).andThen(Integer::parseInt));
    }

    private static String fromChars(FiniteList<Character> chars) {
        return FiniteListFunctional.foldr((c, st) -> st + c.toString(), "", chars);
    }

    static <A> Parser<Maybe<A>> optional(Parser<A> parser) {
        return s -> parser.apply(s).either(_ -> Either.<FiniteList<ParserError>,Pair<Maybe<A>,String>>right(new Pair<>(Maybe.of(), s)),
                pair -> pair.map((elem, output) -> Either.<FiniteList<ParserError>,Pair<Maybe<A>,String>>right(new Pair<>(Maybe.of(elem), output))));
    }

    static <A,B> Parser<FiniteList<A>> sepBy(Parser<A> parser, Parser<B> separator) {
        Parser<Function<FiniteList<A>,FiniteList<A>>> cons = ParserApplicative.map(optional(parser), optA -> lst -> optA.maybe(a -> cons(a,lst), lst));
        Parser<A> parserWithSeparator = (Parser<A>) ApplicativeUtil.keepRight(ParserApplicative.getInstance(), separator, parser);
        return ParserApplicative.fapply(cons, ParserApplicative.many(parserWithSeparator));
    }

}
