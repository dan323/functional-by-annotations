package com.dan323.functional.util;

public class Identity<A> {

    private final A a;

    public Identity(A a){
        this.a = a;
    }

    public A get(){
        return a;
    }

    @Override
    public String toString() {
        return "id["+a.toString()+"]";
    }
}
