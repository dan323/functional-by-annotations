package com.dan323.functional.data;

public abstract class Void {

    private Void(){
        throw new IllegalStateException("Void cannot be instanciated");
    }

    public abstract  <A> A absurd();

}
