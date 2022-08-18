package com.dan323.functional;

import com.dan323.functional.data.FunctionFrom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctionFromTest {

    @Test
    public void functionFromFunctor(){
        var fun = FunctionFrom.<Integer,String,String>map(Integer::parseInt, String::valueOf);

        assertEquals("9", fun.apply("9"));
    }
}
