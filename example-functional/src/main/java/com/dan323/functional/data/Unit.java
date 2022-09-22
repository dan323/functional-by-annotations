package com.dan323.functional.data;

import com.dan323.functional.data.optional.Maybe;

public class Unit {

    private Unit() {
    }

    private static final Unit TT = new Unit();

    public static Unit fromMVoid(Maybe<Void> tt){
        return getElement();
    }

    public static Maybe<Void> toMVoid(Unit unit){
        return Maybe.of();
    }

    public static Unit getElement(){
        return TT;
    }

    @Override
    public String toString(){
        return "()";
    }

    @Override
    public int hashCode() {
        return 101;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }
}
