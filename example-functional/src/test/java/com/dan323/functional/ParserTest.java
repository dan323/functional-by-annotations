package com.dan323.functional;

import com.dan323.functional.data.either.Either;
import com.dan323.functional.data.list.FiniteList;
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
}
