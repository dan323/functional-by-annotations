package com.dan323.functional.data.writer;

import com.dan323.functional.annotation.Monad;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.compiler.util.MonoidUtil;
import com.dan323.functional.annotation.compiler.util.SemigroupUtil;
import com.dan323.functional.annotation.funcs.IMonad;

import java.util.function.BiFunction;
import java.util.function.Function;

@Monad
public class WriterMonad<W> implements IMonad<Writer<?, W>> {

    private final IMonoid<W> monoid;

    public WriterMonad(IMonoid<W> logMonoid) {
        this.monoid = logMonoid;
    }

    @Override
    public Class<Writer<?, W>> getClassAtRuntime() {
        return (Class<Writer<?, W>>) (Class<?>) Writer.class;
    }

    public <A, B> Writer<B, W> map(Writer<A, W> writer, Function<A, B> function) {
        return new Writer<>(function.apply(writer.execute()), writer.log());
    }

    public <A> Writer<A, W> pure(A a) {
        return new Writer<>(a, MonoidUtil.unit(monoid));
    }

    public <A, B> Writer<B, W> fapply(Writer<A, W> base, Writer<Function<A, B>, W> function) {
        return new Writer<>(function.execute().apply(base.execute()), SemigroupUtil.op(monoid, function.log(), base.log()));
    }

    public <A, B> Writer<B, W> flatMap(Function<A, Writer<B, W>> fun, Writer<A, W> writer) {
        var writ = fun.apply(writer.execute());
        return new Writer<>(writ.execute(), SemigroupUtil.op(monoid, writer.log(), writ.log()));
    }

    public <A> Writer<A, W> join(Writer<Writer<A, W>, W> writer) {
        return new Writer<>(writer.execute().execute(), SemigroupUtil.op(monoid, writer.log(), writer.execute().log()));
    }

    public <A, B, C> Writer<C, W> liftA2(BiFunction<A, B, C> function, Writer<A, W> aWriter, Writer<B, W> bWriter) {
        var c = function.apply(aWriter.execute(), bWriter.execute());
        var w = SemigroupUtil.op(monoid, aWriter.log(), bWriter.log());
        return new Writer<>(c, w);
    }
}
