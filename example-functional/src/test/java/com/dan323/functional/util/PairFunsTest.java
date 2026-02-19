package com.dan323.functional.util;

import com.dan323.functional.data.pair.Pair;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PairFunsTest {

    @Test
    public void pairBiMap(){
        Pair<String,Boolean> pair = new Pair<>("howdy", true);
        var result = pair.biMap((s, b) -> b?s.length():s.length()-1, (s, b) -> b?s.toUpperCase():s.toLowerCase());
        assertEquals(new Pair<>(5, "HOWDY"), result);
    }

    @Test
    public void pairMaps(){
        Pair<String,Boolean> pair = new Pair<>("howdy", true);
        var result = pair.mapFirst(String::length).mapSecond(b -> b?"HOWDY":"howdy");
        assertEquals(new Pair<>(5, "HOWDY"), result);
    }

    @Test
    public  void pairCurry(){
        var biFunction = Pair.<String,String,String>biFunction(p -> p.getKey() + " " + p.getValue());
        var result = biFunction.apply("howdy", "partner");
        assertEquals("howdy partner", result);

        var function = Pair.<String,String,String>pairFunction((s1, s2) -> s1 + " " + s2);
        result = function.apply(new Pair<>("howdy", "partner"));
        assertEquals("howdy partner", result);
    }
}
