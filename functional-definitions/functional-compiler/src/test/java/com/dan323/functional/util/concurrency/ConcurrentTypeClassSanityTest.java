package com.dan323.functional.util.concurrency;

import com.dan323.functional.annotation.compiler.util.FunctorUtil;
import com.dan323.functional.util.functor.FunctorMock;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConcurrentTypeClassSanityTest {

    private static final List<Integer> BASE = List.of(1, 2, 3);
    private static final List<Integer> EXPECTED = List.of(2, 3, 4);

    @Test
    public void functorUtilMapIsThreadSafe() throws Exception {
        int threads = 16;
        int tasks = 20;
        int runs = 10;

        for (int run = 0; run < runs; run++) {
            ExecutorService executor = Executors.newFixedThreadPool(threads);
            try {
                CountDownLatch ready = new CountDownLatch(tasks);
                CountDownLatch start = new CountDownLatch(1);
                CountDownLatch done = new CountDownLatch(tasks);
                List<Future<List<Integer>>> futures = new ArrayList<>(tasks);

                for (int i = 0; i < tasks; i++) {
                    futures.add(executor.submit(mapTask(ready, start, done)));
                }

                assertTrue(ready.await(15, TimeUnit.SECONDS), "Workers did not initialize in time.");
                start.countDown();
                assertTrue(done.await(15, TimeUnit.SECONDS), "Workers did not finish in time.");

                for (Future<List<Integer>> future : futures) {
                    assertEquals(EXPECTED, future.get(5, TimeUnit.SECONDS));
                }

                executor.shutdownNow();
                assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS), "Executor did not shut down.");
            } finally {
                executor.shutdownNow();
            }
        }
    }

    private static Callable<List<Integer>> mapTask(CountDownLatch ready, CountDownLatch start, CountDownLatch done) {
        return () -> {
            ready.countDown();
            boolean started = start.await(10, TimeUnit.SECONDS);
            assertTrue(started, "Workers did not start in time.");
            try {
                return (List<Integer>) FunctorUtil.map(FunctorMock.FUNCTOR, BASE, (Integer x) -> x + 1);
            } finally {
                done.countDown();
            }
        };
    }
}

