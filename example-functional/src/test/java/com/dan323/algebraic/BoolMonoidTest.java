package com.dan323.algebraic;

import com.dan323.functional.data.bool.AndMonoid;
import com.dan323.functional.data.bool.OrMonoid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoolMonoidTest {

    @Test
    public void andTest(){
        assertEquals(true, AndMonoid.op(true, true));
        assertEquals(false, AndMonoid.op(false,true));
        assertEquals(false, AndMonoid.op(true,false));
        assertEquals(false, AndMonoid.op(false,false));
        assertEquals(true, AndMonoid.unit());
    }

    @Test
    public void orTest(){
        assertEquals(true, OrMonoid.op(true, true));
        assertEquals(true, OrMonoid.op(false,true));
        assertEquals(true, OrMonoid.op(true,false));
        assertEquals(false, OrMonoid.op(false,false));
        assertEquals(false, OrMonoid.unit());
    }
}
