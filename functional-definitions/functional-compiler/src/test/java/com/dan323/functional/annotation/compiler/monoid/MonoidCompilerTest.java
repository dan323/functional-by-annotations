package com.dan323.functional.annotation.compiler.monoid;

import com.dan323.functional.annotation.compiler.FunctionalCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonoidCompilerTest {
    @Test
    public void monoidRun() {
        List<String> args = Stream.of("../annotation-definitions/src/main/java/com/dan323/functional/annotation/Monoid",
                        "src/test/java/com/dan323/functional/annotation/compiler/monoid/MonoidInteger"
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
