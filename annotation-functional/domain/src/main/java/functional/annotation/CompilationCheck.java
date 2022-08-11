package functional.annotation;

public interface CompilationCheck {

    Compiler getCompiler();

    interface Compiler{
        void compile();
    }

}
