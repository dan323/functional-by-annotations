package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.parser.Parser;
import com.dan323.functional.data.parser.ParserSupplierApplicative;
import org.junit.jupiter.api.Test;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserSupplierApplicativeTest {

    @Test
    public void pureDoesNotConsumeInput() {
        var parser = ParserSupplierApplicative.pure(42).get();
        assertEquals(Either.right(new Pair<>(42, "abc")), parser.apply("abc"));
    }

    @Test
    public void mapTransformsParsedValue() {
        Supplier<Parser<Integer>> base = Parser::intParser;
        var mapped = ParserSupplierApplicative.map(base, x -> x + 1).get();
        assertEquals(Either.right(new Pair<>(124, "")), mapped.apply("123"));
    }

    @Test
    public void fapplyRunsFunctionThenValueParser() {
        Supplier<Parser<Function<Character, String>>> fun =
                () -> ParserSupplierApplicative.map(() -> Parser.charParser('a'), a -> (Function<Character,String>)(b -> "" + a + b)).get();
        Supplier<Parser<Character>> st = () -> Parser.charParser('b');
        var applied = ParserSupplierApplicative.fapply(fun, st).get();
        assertEquals(Either.right(new Pair<>("ab", "")), applied.apply("ab"));
    }

    @Test
    public void emptyReturnsParserError() {
        var empty = ParserSupplierApplicative.empty().get();
        assertEquals(Either.left(FiniteList.of(Parser.ParserError.empty())), empty.apply("abc"));
    }

    @Test
    public void disjunctionFallsBackOnFailure() {
        var parser = ParserSupplierApplicative.disjunction(
                () -> Parser.stringParser("x"),
                () -> Parser.stringParser("a")
        ).get();
        assertEquals(Either.right(new Pair<>("a", "bc")), parser.apply("abc"));
    }
}

