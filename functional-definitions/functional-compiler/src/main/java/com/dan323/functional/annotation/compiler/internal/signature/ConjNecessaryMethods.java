package com.dan323.functional.annotation.compiler.internal.signature;

import javax.lang.model.element.ExecutableElement;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation for a conjunction of requirements
 *
 * @author daniel
 */
public class ConjNecessaryMethods implements NecessaryMethods {

    private final Set<NecessaryMethods> methods;

    public ConjNecessaryMethods(NecessaryMethods methods1, NecessaryMethods methods2) {
        this(Set.of(methods1, methods2));
    }

    public ConjNecessaryMethods(Set<NecessaryMethods> methodsSet) {
        this.methods = methodsSet;
    }

    @Override
    public NecessaryMethods process(ExecutableElement method) {
        Set<NecessaryMethods> newSet = this.methods.stream()
                .map(nec -> nec.process(method))
                .filter(nec -> !(nec instanceof EmptyNecessaryMethods))
                .collect(Collectors.toSet());
        if (newSet.size() == 1) {
            return newSet.stream().findFirst().get();
        } else if (newSet.isEmpty()) {
            return new EmptyNecessaryMethods();
        } else {
            return new ConjNecessaryMethods(newSet);
        }
    }

    @Override
    public String toString() {
        return "[" + methods.stream().map(Objects::toString).collect(Collectors.joining(" AND ")) + "]";
    }
}
