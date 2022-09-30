package com.dan323.functional.data.writer;

import com.dan323.functional.data.Unit;

public class Writer<A, W> {
    private final W writer;
    private final A element;

    Writer(A element, W writer) {
        this.element = element;
        this.writer = writer;
    }

    public static <W> Writer<Unit, W> tell(W write) {
        return new Writer<>(Unit.getElement(), write);
    }

    public W log(){
        return writer;
    }

    public A execute(){
        return element;
    }

}
