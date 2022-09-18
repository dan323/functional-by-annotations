package com.dan323.functional.util.alternative;

import com.dan323.functional.annotation.compiler.util.AlternativeUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlternativeUtilTest {

    @Test
    public void emptyTest() {
        var emp = AlternativeUtil.empty(new AlternativeMock());
        assertEquals(List.of(), emp);
    }

    @Test
    public void disjunctionTest() {
        var concat = AlternativeUtil.disj(new AlternativeMock(), List.of(1, 2), List.of(3, 4));
        assertEquals(List.of(1, 2, 3, 4), concat);
    }
}
