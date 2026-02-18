package com.dan323.functional.annotation.compiler.internal;

import com.dan323.functional.annotation.*;
import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.IRing;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.compiler.internal.signature.StructureSignatures;
import com.dan323.functional.annotation.funcs.*;

import javax.annotation.processing.Messager;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.function.BiFunction;

public final class CompilerFactory {

    private static final Map<Class<?>, CompilerFactoryEntry<?>> INTERFACE_TO_COMPILER = buildCompilerMap();

    private record CompilerFactoryEntry<A extends Annotation>(
            String interfaceTypeName,
            Class<A> annotationClass,
            BiFunction<StructureSignatures, DeclaredType, com.dan323.functional.annotation.compiler.internal.signature.NecessaryMethods> signatureChecker
    ) {}

    private static Map<Class<?>, CompilerFactoryEntry<?>> buildCompilerMap() {
        var map = new HashMap<Class<?>, CompilerFactoryEntry<?>>();

        map.put(IFunctor.class, new CompilerFactoryEntry<>(
                IFunctor.class.getTypeName(),
                Functor.class,
                StructureSignatures::functorSignatureChecker
        ));
        map.put(IApplicative.class, new CompilerFactoryEntry<>(
                IApplicative.class.getTypeName(),
                Applicative.class,
                StructureSignatures::applicativeSignatureChecker
        ));
        map.put(IMonad.class, new CompilerFactoryEntry<>(
                IMonad.class.getTypeName(),
                Monad.class,
                StructureSignatures::monadSignatureChecker
        ));
        map.put(ISemigroup.class, new CompilerFactoryEntry<>(
                ISemigroup.class.getTypeName(),
                Semigroup.class,
                StructureSignatures::semigroupSignatureChecker
        ));
        map.put(IMonoid.class, new CompilerFactoryEntry<>(
                IMonoid.class.getTypeName(),
                Monoid.class,
                StructureSignatures::monoidSignatureChecker
        ));
        map.put(IFoldable.class, new CompilerFactoryEntry<>(
                IFoldable.class.getTypeName(),
                Foldable.class,
                StructureSignatures::foldableSignatureChecker
        ));
        map.put(IAlternative.class, new CompilerFactoryEntry<>(
                IAlternative.class.getTypeName(),
                Alternative.class,
                StructureSignatures::alternativeSignatureChecker
        ));
        map.put(IRing.class, new CompilerFactoryEntry<>(
                IRing.class.getTypeName(),
                Ring.class,
                StructureSignatures::ringSignatureChecker
        ));
        map.put(ITraversal.class, new CompilerFactoryEntry<>(
                ITraversal.class.getTypeName(),
                Traversal.class,
                StructureSignatures::traversalSignatureChecker
        ));

        return map;
    }

    /**
     * Get all supported functional annotations
     * @return Set of all supported functional annotation classes
     */
    public static Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return INTERFACE_TO_COMPILER.values().stream()
                .map(entry -> entry.annotationClass)
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));
    }

    /**
     * Get mapping from annotation to interface name
     * @return Map of annotation class to interface name
     */
    public static Map<Class<? extends Annotation>, String> getAnnotationToInterfaceMap() {
        return INTERFACE_TO_COMPILER.values().stream()
                .collect(java.util.stream.Collectors.toMap(
                        entry -> entry.annotationClass,
                        entry -> entry.interfaceTypeName.substring(entry.interfaceTypeName.lastIndexOf('.') + 1)
                ));
    }

    /**
     * Create a compiler depending on what element we are compiling
     *
     * @param iface    interface to check at compile time
     * @param elements utility class
     * @param types    utility class
     * @param messager instance to manage errors
     * @return A compiler compatible with {@code iface}
     */
    public Compiler<?> from(DeclaredType iface, Elements elements, Types types, Messager messager) {
        StructureSignatures signatures = new StructureSignatures(elements, types);

        for (var entry : INTERFACE_TO_COMPILER.entrySet()) {
            var compilerEntry = entry.getValue();
            if (iface.asElement().equals(elements.getTypeElement(compilerEntry.interfaceTypeName))) {
                return new Compiler<>(
                        compilerEntry.signatureChecker.apply(signatures, iface),
                        compilerEntry.annotationClass,
                        messager
                );
            }
        }

        throw new IllegalArgumentException(String.format("The interfaces %s does not represent an implemented functional", iface));
    }
}
