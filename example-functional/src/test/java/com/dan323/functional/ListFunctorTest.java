package com.dan323.functional;

import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.list.ZipApplicative;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ListFunctorTest {

    @Test
    public void constInfiniteMap() {
        var sol = ZipApplicative.map(List.cons(5, List.repeat(6)), x -> x * 2);
        assertEquals(FiniteList.of(10, 12, 12), sol.limit(3));
    }

    @Test
    public void emptyListFunctor() {
        var sol = ZipApplicative.map(List.nil(), Object::toString);
        assertEquals(List.nil(), sol);
    }

    @Test
    public void finiteListAsListFunctor() {
        var sol = ZipApplicative.map(FiniteList.of(1, 2, 3), x -> x * 2);
        assertEquals(FiniteList.of(2, 4, 6), sol);
    }

    @Test
    public void repeatListFunctor() {
        var sol = ZipApplicative.map(List.repeat(5), x -> x * 3);
        assertEquals(List.repeat(15).limit(10), sol.limit(10));
    }

    @Test
    public void generatingListFunctor() {
        // Since this type of lists cannot be checked in all their entries, we will test the first 10 elements
        var sol = ZipApplicative.map(List.generate(1, x -> x + 1), x -> x + 1);
        assertEquals(List.generate(2, x -> x + 1).limit(10), sol.limit(10));

        var sol2 = ZipApplicative.map(List.generate(1, x -> x + 1), x -> x * 2);
        assertEquals(List.generate(2, x -> x + 2).limit(10), sol2.limit(10));
        sol2 = ZipApplicative.map(sol2, x -> x + 1);
        assertEquals(List.generate(3, x -> x + 2).limit(10), sol2.limit(10));
    }

}
