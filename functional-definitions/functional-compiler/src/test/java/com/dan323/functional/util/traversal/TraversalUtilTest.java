package com.dan323.functional.util.traversal;

import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.annotation.compiler.util.TraversalUtil;
import com.dan323.functional.util.Identity;
import com.dan323.functional.util.applicative.ApplicativeMock;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TraversalUtilTest {

    @Test
    public void traverseTest() {
        TraversalList tList = new TraversalList();
        var sol = tList.traverse(ApplicativeMock.APPLICATIVE, (Integer x) -> new Identity<>(x + 6), List.of(1, 2, 3));
        assertEquals(List.of(7, 8, 9), sol.get());
        var sol2 = TraversalUtil.traverse(new TraversalSequenceA(), ApplicativeMock.APPLICATIVE, (Integer x) -> new Identity<>(x + 6), List.of(1, 2, 3));
        assertEquals(List.of(7, 8, 9), sol2.get());
    }

    @Test
    public void sequenceATest() {
        TraversalList tList = new TraversalList();
        var sol = TraversalUtil.sequenceA(tList, ApplicativeMock.APPLICATIVE, Stream.of(1, 2, 3).map(Identity::new).toList());
        assertEquals(List.of(1, 2, 3), sol.get());
        var sol2 = (new TraversalSequenceA()).sequenceA(ApplicativeMock.APPLICATIVE, (List) Stream.of(1, 2, 3).map(Identity::new).toList());
        assertEquals(List.of(1, 2, 3), sol.get());
    }

    @Test
    public void sequenceAMapTest() {
        TraversalList tList = new TraversalList();
        var sol = FunctorUtil.map(tList, List.of(1, 2, 3), (Integer x) -> x * 5 - 2);
        assertEquals(List.of(3, 8, 13), sol);
        TraversalSequenceA tList2 = new TraversalSequenceA();
        sol = FunctorUtil.map(tList2, List.of(1, 2, 3), (Integer x) -> x + 1);
        assertEquals(List.of(2, 3, 4), sol);
    }
}
