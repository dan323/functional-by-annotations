package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation for a disjunction of requirements
 *
 * @author daniel
 */
public class DisjNecessaryMethods implements NecessaryMethods {
    private final Set<NecessaryMethods> methods;

    public DisjNecessaryMethods(NecessaryMethods methods1, NecessaryMethods methods2) {
        this(Set.of(methods1, methods2));
    }

    public DisjNecessaryMethods(Set<NecessaryMethods> methodsSet) {
        this.methods = methodsSet;
    }

    @Override
    public NecessaryMethods process(ExecutableElement method) {
        Set<NecessaryMethods> newMethods = this.methods.stream().map(nec -> nec.process(method)).collect(Collectors.toSet());
        var anyMatch = newMethods.stream().anyMatch(nec -> nec instanceof EmptyNecessaryMethods);
        if (anyMatch) {
            return new EmptyNecessaryMethods();
        } else {
            return new DisjNecessaryMethods(newMethods);
        }
    }

    @Override
    public String toString() {
        return "[" + methods.stream().map(Objects::toString).collect(Collectors.joining(" OR ")) + "]";
    }
}
