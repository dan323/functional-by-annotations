module functional.data {
    requires functional.annotations;
    requires functional.compiler;

    exports com.dan323.functional.data.continuation;
    exports com.dan323.functional.data.either;
    exports com.dan323.functional.data.function;
    exports com.dan323.functional.data.list;
    exports com.dan323.functional.data.list.zipper;
    exports com.dan323.functional.data.optional;
    exports com.dan323.functional.data.tree;
    exports com.dan323.functional.data.bool;
    exports com.dan323.functional.data.integer;
    exports com.dan323.functional.data.pair;
    exports com.dan323.functional.data.util.applicative;
    exports com.dan323.functional.data.util.alternative;
    exports com.dan323.functional.data.util.foldable;
    exports com.dan323.functional.data.state;
    exports com.dan323.functional.data.stack;
}