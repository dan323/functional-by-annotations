package com.dan323.functional.data.pair;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class Pair<U, V> implements Map.Entry<U, V> {

    private final Map.Entry<U, V> entry;

    public Pair(U u, V v) {
        entry = new AbstractMap.SimpleImmutableEntry<>(u, v);
    }

    @Override
    public U getKey() {
        return entry.getKey();
    }

    public static <A,B,C> BiFunction<A,B,C> biFunction(Function<Pair<A,B>,C> function) {
        return ((BiFunction<A,B,Pair<A,B>>)(Pair::new)).andThen(function);
    }

    public static <A,B,C> Function<Pair<A,B>,C> pairFunction(BiFunction<A,B,C> biFunction) {
        return p -> biFunction.apply(p.getKey(), p.getValue());
    }

    @Override
    public V getValue() {
        return entry.getValue();
    }

    @Override
    public V setValue(V v) {
        return entry.setValue(v);
    }

    public <U1> Pair<U1, V> mapFirst(Function<U, U1> function) {
        return new Pair<>(function.apply(entry.getKey()), entry.getValue());
    }

    public <V1> Pair<U, V1> mapSecond(Function<V, V1> function) {
        return new Pair<>(entry.getKey(), function.apply(entry.getValue()));
    }

    public <S, T> Pair<S, T> biMap(BiFunction<U, V, S> first, BiFunction<U, V, T> second) {
        return new Pair<>(first.apply(entry.getKey(), entry.getValue()), second.apply(entry.getKey(), entry.getValue()));
    }

    public <S> S map(BiFunction<U, V, S> first) {
        return first.apply(entry.getKey(), entry.getValue());
    }

    public String toString() {
        return "{(" + getKey() + " , " + getValue() + ")}";
    }

    @Override
    public int hashCode() {
        return getValue().hashCode() * 13 - 5 * getKey().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Pair<?, ?> pair) {
            return Objects.equals(pair.getKey(), getKey()) && Objects.equals(pair.getValue(), getValue());
        } else {
            return false;
        }
    }
}
