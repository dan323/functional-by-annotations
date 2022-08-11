package functional.data.list;

import functional.data.optional.Maybe;

import java.util.function.Function;
import java.util.function.UnaryOperator;

final class Generating<A> implements List<A> {

    private final A head;
    private final UnaryOperator<A> generator;

    Generating(A first, UnaryOperator<A> generator){
        if (first == null || generator == null) throw new IllegalArgumentException("Inputs must not be null");
        this.head = first;
        this.generator = generator;
    }

    @Override
    public Maybe<A> head() {
        return Maybe.of(head);
    }

    @Override
    public List<A> tail() {
        return new Generating<>(generator.apply(head), generator);
    }

    @Override
    public <B> List<B> map(Function<A, B> mapping) {
        return new GeneratingMapped<>(this, mapping);
    }

    protected static final class GeneratingMapped<A,B> implements List<B> {

        private final List<A> originalList;
        private final Function<A,B> mapping;

        GeneratingMapped(List<A> originalList, Function<A,B> mapping){
            this.mapping = mapping;
            this.originalList = originalList;
        }

        @Override
        public Maybe<B> head() {
            return originalList.head().maybe(h -> Maybe.of(mapping.apply(h)), Maybe.of());
        }

        @Override
        public List<B> tail() {
            return new GeneratingMapped<>(originalList.tail(), mapping);
        }

        @Override
        public <B1> List<B1> map(Function<B, B1> mapping) {
            return new GeneratingMapped<>(originalList, mapping.compose(this.mapping));
        }
    }
}
