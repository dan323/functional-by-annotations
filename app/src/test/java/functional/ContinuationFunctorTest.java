package functional;

import functional.annotation.iface.FunctorUtils;
import functional.data.continuation.Continuation;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContinuationFunctorTest {

    @Test
    public void continuationTest() {
        Function<Integer, Continuation<Integer, String>> succ = q -> k -> k.apply(q + 1);

        assertEquals("7", succ.apply(6).apply(k -> Integer.toString(k)));
    }

    @Test
    public void continuationFunctor(){
        Function<Integer, Continuation<Integer, String>> succ = q -> k -> k.apply(q + 1);
        Continuation<Integer,String> mapped = Continuation.map(succ.apply(7), k -> k+2);

        assertEquals("10", mapped.apply(k -> Integer.toString(k)));

        Continuation<Integer,String> cont = FunctorUtils.<Continuation,Continuation<?,String>,Continuation<Integer,String>,Continuation<Integer,String>,Integer,Integer>map(Continuation.class, succ.apply(9), k -> k-1);
        assertEquals("9", cont.apply(k -> Integer.toString(k)));
    }
}
