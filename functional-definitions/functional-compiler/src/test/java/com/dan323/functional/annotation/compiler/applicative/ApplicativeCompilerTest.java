package com.dan323.functional.annotation.compiler.applicative;

import com.dan323.functional.annotation.compiler.FunctionalCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicativeCompilerTest {

    @Test
    public void applicativeFapplyRun() {
        List<String> args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Applicative",
                        "src/test/java/com/dan323/functional/annotation/compiler/applicative/ApplicativeMock")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }

    @Test
    public void applicativeNoPublicClass(){
        var args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Functor",
                        "src/test/java/com/dan323/functional/annotation/compiler/applicative/NoPublicTypeApplicative")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        var flags = args.toArray(new String[4]);
        var k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }

    @Test
    public void applicativeFunctor(){
        var args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Functor",
                        "src/test/java/com/dan323/functional/annotation/compiler/applicative/ApplicativeFunctor")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        var flags = args.toArray(new String[4]);
        var k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }


    @Test
    public void applicativeIncomplete(){
        var args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Functor",
                        "src/test/java/com/dan323/functional/annotation/compiler/applicative/ApplicativeOnlyPure")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        var flags = args.toArray(new String[4]);
        var k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }

    @Test
    public void applicativeLiftA2Run() {
        List<String> args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Applicative",
                        "src/test/java/com/dan323/functional/annotation/compiler/applicative/ApplicativeLiftMock")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }

}
