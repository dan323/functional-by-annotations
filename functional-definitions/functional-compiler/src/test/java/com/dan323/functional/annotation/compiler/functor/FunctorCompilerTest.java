package com.dan323.functional.annotation.compiler.functor;

import com.dan323.functional.annotation.compiler.FunctionalCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FunctorCompilerTest {

    @Test
    public void eitherFunctor(){
        List<String> args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/functor/EitherF")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);

    }
    @Test
    public void functorNoPublicFun() {
        List<String> args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/functor/NoPublicMapFunctor")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }

    @Test
    public void functorMonad(){
        List<String> args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/functor/FunctorMonad")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }

    @Test
    public void functorNoPublicClass(){
        var args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/functor/NoPublicTypeFunctor")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        var flags = args.toArray(new String[4]);
        var k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }

    @Test
    public void functorNoStaticRun() {
        List<String> args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/functor/NoStaticFunctor")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }

    @Test
    public void functorRun() {
        List<String> args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/functor/FunctorMock")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }
}
