package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.optional.Maybe;
import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.parser.Parser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {

    @Test
    void parseIntFull() {
        var pair = Parser.intParser().apply("123");
        assertEquals(Either.right(new Pair<>(123, "")), pair);
    }


    @Test
    void parseIntPartial() {
        var pair = Parser.intParser().apply("123ca");
        assertEquals(Either.right(new Pair<>(123, "ca")), pair);
    }

    @Test
    void parseIntFail() {
        var pair = Parser.intParser().apply("ca");
        assertEquals(Either.<FiniteList<Parser.ParserError>, Pair<Integer, String>>left(FiniteList.of(Parser.ParserError.unexpectedChar('c'))), pair);

        pair = Parser.intParser().apply("");
        assertEquals(Either.<FiniteList<Parser.ParserError>, Pair<Integer, String>>left(FiniteList.of(Parser.ParserError.unexpectedEnd())), pair);
    }

    @Test
    void optionalParseSuccess() {
        var backtrackInt = Parser.optional(Parser.intParser());
        var result = backtrackInt.apply("123abc");
        assertEquals(Either.right(new Pair<>(Maybe.of(123), "abc")), result);
    }


    @Test
    void optionalParseSuccessEmpty() {
        var backtrackInt = Parser.optional(Parser.intParser());
        var result = backtrackInt.apply("abc");
        assertEquals(Either.right(new Pair<>(Maybe.of(), "abc")), result);
    }

    @Test
    void sepByWithOneElement() {
        var sepByInt = Parser.sepBy(Parser.intParser(), Parser.stringParser(","));
        var result = sepByInt.apply("123");
        assertEquals(Either.right(new Pair<>(FiniteList.of(123), "")), result);
    }

    @Test
    void sepByWithMultipleElements() {
        var sepByInt = Parser.sepBy(Parser.intParser(), Parser.stringParser(","));
        var result = sepByInt.apply("1,2,3");
        assertEquals(Either.right(new Pair<>(FiniteList.of(1, 2, 3), "")), result);
    }

    @Test
    void sepByEmpty() {
        var sepByInt = Parser.sepBy(Parser.intParser(), Parser.stringParser(","));
        var result = sepByInt.apply("abc");
        assertEquals(Either.right(new Pair<>(FiniteList.nil(), "abc")), result);
    }

    @Test
    void sepByWithTrailingInput() {
        var sepByInt = Parser.sepBy(Parser.intParser(), Parser.stringParser(","));
        var result = sepByInt.apply("1,2,3xyz");
        assertEquals(Either.right(new Pair<>(FiniteList.of(1, 2, 3), "xyz")), result);
    }

}
