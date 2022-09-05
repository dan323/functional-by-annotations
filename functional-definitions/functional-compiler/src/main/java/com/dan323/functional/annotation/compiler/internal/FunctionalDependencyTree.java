package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.Structure;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.*;

import java.util.ArrayList;
import java.util.List;

class FunctionalDependencyTree {

    private final Class<? extends Structure> node;
    private final List<FunctionalDependencyTree> dependency;

    private FunctionalDependencyTree(Class<? extends Structure> node) {
        this.node = node;
        this.dependency = new ArrayList<>();
    }

    private void addDependency(FunctionalDependencyTree dependencyTree) {
        dependency.add(dependencyTree);
    }

    /**
     * Obtain the structural dependency tree
     *
     * @return the dependency tree of all the {@link Structure}
     */
    public static List<FunctionalDependencyTree> getActualTree() {
        FunctionalDependencyTree monad = new FunctionalDependencyTree(IMonad.class);
        FunctionalDependencyTree applicative = new FunctionalDependencyTree(IApplicative.class);
        FunctionalDependencyTree functor = new FunctionalDependencyTree(IFunctor.class);
        FunctionalDependencyTree semigroup = new FunctionalDependencyTree(ISemigroup.class);
        FunctionalDependencyTree monoid = new FunctionalDependencyTree(IMonoid.class);
        FunctionalDependencyTree foldable = new FunctionalDependencyTree(IFoldable.class);
        FunctionalDependencyTree traversal = new FunctionalDependencyTree(ITraversal.class);
        monad.addDependency(applicative);
        applicative.addDependency(functor);
        monoid.addDependency(semigroup);
        traversal.addDependency(functor);
        traversal.addDependency(foldable);
        return List.of(monad, monoid, traversal);
    }

    public Class<? extends Structure> getNode() {
        return node;
    }

    public static List<FunctionalDependencyTree> getNextLine(FunctionalDependencyTree tree) {
        return tree.dependency;
    }
}
