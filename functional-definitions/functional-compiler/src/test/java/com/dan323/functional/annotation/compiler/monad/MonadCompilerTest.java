package com.dan323.functional.annotation.compiler.monad;

import com.dan323.functional.annotation.compiler.FunctionalCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonadCompilerTest {
    @Test
    public void monadFlatMapRun() {
        List<String> args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Monad",
                        "src/test/java/com/dan323/functional/annotation/compiler/monad/MonadFlatMap"
                )
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }

    @Test
    public void monadJoinFunctorRun() {
        List<String> args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Monad",
                        "src/test/java/com/dan323/functional/annotation/compiler/monad/MonadJoinFunctor"
                )
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }

    @Test
    public void monadJoinApplicativeRun() {
        List<String> args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Monad",
                        "src/test/java/com/dan323/functional/annotation/compiler/monad/MonadJoinApplicativeFapply"
                )
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }

    @Test
    public void monadJoinApplicative2Run() {
        List<String> args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Monad",
                        "src/test/java/com/dan323/functional/annotation/compiler/monad/MonadJoinApplicativeLift"
                )
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }
}
