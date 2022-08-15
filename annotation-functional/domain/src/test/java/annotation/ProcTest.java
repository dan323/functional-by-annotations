package annotation;

import functional.annotation.FunctionalCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProcTest {
    @Test
    public void functorWrongRun() {
        var basePath = Paths.get("");
        System.out.println(basePath.toAbsolutePath());
        List<String> args = Stream.of("../annotation/src/main/java/functional/annotation/Functor",
                        "src/test/java/annotation/WrongFunctorMock")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(1, k);
    }

    @Test
    public void functorRun() {
        var basePath = Paths.get("");
        System.out.println(basePath.toAbsolutePath());
        List<String> args = Stream.of("../annotation/src/main/java/functional/annotation/Functor",
                        "src/test/java/annotation/FunctorMock")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }

    @Test
    public void applicativeRun() {
        var basePath = Paths.get("");
        System.out.println(basePath.toAbsolutePath());
        List<String> args = Stream.of("../annotation/src/main/java/functional/annotation/Applicative",
                        "src/test/java/annotation/ApplicativeMock")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctionalCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        int k = ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
        assertEquals(0, k);
    }
}
