module functional.compiler {
    requires java.compiler;
    requires functional.annotations;

    provides javax.annotation.processing.Processor with com.dan323.functional.annotation.compiler.FunctionalCompiler;

    exports com.dan323.functional.annotation.compiler;
}