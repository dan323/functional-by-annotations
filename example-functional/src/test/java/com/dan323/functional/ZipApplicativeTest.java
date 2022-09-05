package com.dan323.functional;

import com.dan323.functional.annotation.compiler.util.ApplicativeUtil;
import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.data.list.FiniteList;
import com.dan323.functional.data.list.List;
import com.dan323.functional.data.list.ZipApplicative;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZipApplicativeTest {

    @Test
    public void zipPure() {
        ZipApplicative zipApplicative = new ZipApplicative();
        List<Integer> lst = (List<Integer>) ApplicativeUtil.pure(zipApplicative, 6);
        assertEquals(FiniteList.of(6, 6, 6, 6, 6, 6), lst.limit(6));
    }

    @Test
    public void zipSum() {
        ZipApplicative zipApplicative = new ZipApplicative();
        List<Integer> lst = (List<Integer>) ApplicativeUtil.liftA2(zipApplicative, Integer::sum, List.repeat(5), FiniteList.of(1, 2, 3));
        assertEquals(FiniteList.of(6, 7, 8), lst);
        lst = (List<Integer>) ApplicativeUtil.liftA2(zipApplicative, Integer::sum, FiniteList.of(1, 2, 3), List.repeat(5));
        assertEquals(FiniteList.of(6, 7, 8), lst);
    }

    @Test
    public void zipGenerating() {
        ZipApplicative zipApplicative = new ZipApplicative();
        List<Integer> lst = (List<Integer>) ApplicativeUtil.liftA2(zipApplicative, Integer::sum, List.repeat(5), List.generate(1, x -> x * 2));
        assertEquals(FiniteList.of(6, 7, 9), lst.limit(3));
        lst = (List<Integer>) ApplicativeUtil.liftA2(zipApplicative, Integer::sum, List.generate(1, x -> x * 2), List.repeat(5));
        assertEquals(FiniteList.of(6, 7, 9), lst.limit(3));
        lst = (List<Integer>) ApplicativeUtil.liftA2(zipApplicative, Integer::sum, List.generate(1, x -> x + 1), List.generate(1, x -> x * 2));
        assertEquals(FiniteList.of(2, 4, 7), lst.limit(3));
    }

    @Test
    public void zippedMap() {
        ZipApplicative zipApplicative = new ZipApplicative();
        List<Integer> lst = (List<Integer>) FunctorUtil.map(zipApplicative, ApplicativeUtil.liftA2(zipApplicative, Integer::sum, List.generate(1, x -> x + 1), List.generate(1, x -> x * 2)), (Integer x) -> x - 1);
        assertEquals(FiniteList.of(1, 3, 6), lst.limit(3));
    }
}
