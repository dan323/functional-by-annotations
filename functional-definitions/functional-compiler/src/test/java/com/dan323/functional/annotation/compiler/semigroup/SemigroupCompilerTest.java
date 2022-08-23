package com.dan323.functional.annotation.compiler.semigroup;

import com.dan323.functional.annotation.compiler.FunctionalCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SemigroupCompilerTest {
    @Test
    public void semigroupRun() {
        List<String> args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/semigroup/SemigroupInteger")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }
    @Test
    public void semigroupNotPublicError() {
        List<String> args = Stream.of("src/test/java/com/dan323/functional/annotation/compiler/monoid/NotPublicTypeSemigroup")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName(), "-p", "../annotation-definitions/target/annotation-definitions-1.0-SNAPSHOT.jar", "--add-modules", "functional.annotations"));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }
}
