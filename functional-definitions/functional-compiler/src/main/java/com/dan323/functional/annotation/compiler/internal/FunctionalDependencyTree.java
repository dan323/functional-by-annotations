package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.Structure;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.funcs.*;

import java.util.ArrayList;
import java.util.List;

class FunctionalDependencyTree {

    private final Class<? extends Structure> node;
    private final List<FunctionalDependencyTree> dependency;

    public FunctionalDependencyTree(Class<? extends Structure> node){
        this.node = node;
        this.dependency = new ArrayList<>();
    }

    public void addDependency(FunctionalDependencyTree dependencyTree){
        dependency.add(dependencyTree);
    }

    public static List<FunctionalDependencyTree> getActualTree(){
        FunctionalDependencyTree monad = new FunctionalDependencyTree(IMonad.class);
        FunctionalDependencyTree applicative = new FunctionalDependencyTree(IApplicative.class);
        FunctionalDependencyTree functor = new FunctionalDependencyTree(IFunctor.class);
        FunctionalDependencyTree semigroup = new FunctionalDependencyTree(ISemigroup.class);
        monad.addDependency(applicative);
        applicative.addDependency(functor);
        return List.of(monad, semigroup);
    }

    public Class<? extends Structure> getNode(){
        return node;
    }

    public static List<FunctionalDependencyTree> getNextLine(FunctionalDependencyTree tree){
        return tree.dependency;
    }
}
