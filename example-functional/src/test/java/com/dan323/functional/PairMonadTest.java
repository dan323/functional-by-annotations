package com.dan323.functional;

import com.dan323.functional.data.Identity;
import com.dan323.functional.data.IdentityMonad;
import com.dan323.functional.data.pair.Pair;
import com.dan323.functional.data.pair.PairFirstMonad;
import com.dan323.functional.data.pair.PairSecondMonad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PairMonadTest {
    @Test
    public void testTraverseIdentity() {
        var pair = new Pair<>(5, "foo");
        var applicative = new IdentityMonad();
        var traversal1 = PairFirstMonad.<String>getInstance();
        var traversal2 = PairSecondMonad.<Integer>getInstance();
        var result1 = traversal1.traverse(applicative, (Integer x) -> Identity.of(x * 2), pair);
        assertEquals(new Pair<>(10, "foo"), Identity.runIdentity(result1));
        var result2 = traversal2.traverse(applicative, (String x) -> Identity.of(x + "2"), pair);
        assertEquals(new Pair<>(5, "foo2"), Identity.runIdentity(result2));
    }

    @Test
    public void testMap() {
        var pair = new Pair<>(3, "bar");
        var traversal1 = PairFirstMonad.<String>getInstance();
        var traversal2 = PairSecondMonad.<Integer>getInstance();
        var mapped1 = traversal1.map(pair, (Integer x) -> x + 7);
        assertEquals(new Pair<>(10, "bar"), mapped1);
        var mapped2 = traversal2.map(pair, (String x) -> x + "7");
        assertEquals(new Pair<>(3, "bar7"), mapped2);
    }

}
