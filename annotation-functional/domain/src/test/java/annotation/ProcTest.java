package annotation;

import functional.annotation.FunctorCompiler;
import org.junit.jupiter.api.Test;

import javax.tools.ToolProvider;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProcTest {
    @Test
    public void run() {
        var basePath = Paths.get("");
        System.out.println(basePath.toAbsolutePath().toString());
        List<String> args = Stream.of("../annotation/src/main/java/functional/annotation/Functor",
                        "src/test/java/annotation/FunctorMock")
                .map(s -> Paths.get(s + ".java").toAbsolutePath().toString())
                .collect(Collectors.toList());
        args.addAll(0, List.of("-processor", FunctorCompiler.class.getName()));
        String[] flags = args.toArray(new String[4]);
        ToolProvider.getSystemJavaCompiler()
                .run(System.in, System.out, System.err, flags);
    }
}
