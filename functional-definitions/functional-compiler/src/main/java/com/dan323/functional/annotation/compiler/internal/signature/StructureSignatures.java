package com.dan323.functional.annotation.compiler.internal.signature;

import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.IRing;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.compiler.internal.CompilerUtils;
import com.dan323.functional.annotation.funcs.*;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Helper class including all implementations of {@link Signature} and {@link NecessaryMethods} for all {@link com.dan323.functional.annotation.Structure}
 *
 * @author daniel
 */
public final class StructureSignatures {

    private final Elements elementUtils;
    private final Types typeUtils;

    public StructureSignatures(Elements elements, Types types) {
        this.elementUtils = elements;
        this.typeUtils = types;
    }

    private Signature createMapSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 2) {
            return new Signature(List.of(CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0)),
                    typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), params.get(1))),
                    CompilerUtils.changeWildBy(typeUtils, iFace, params.get(1)), IFunctor.MAP_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createPureSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 1) {
            return new Signature(List.of(params.get(0)), CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0)), IApplicative.PURE_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createSumSignature(ExecutableType method, DeclaredType iFace) {
        var params = iFace.getTypeArguments();
        if (params.size() >= 1) {
            return new Signature(List.of(), typeUtils.getDeclaredType(elementUtils.getTypeElement(IMonoid.class.getTypeName()), params.get(0)), IRing.SUM_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createProdSignature(ExecutableType method, DeclaredType iFace) {
        var params = iFace.getTypeArguments();
        if (params.size() >= 1) {
            return new Signature(List.of(), typeUtils.getDeclaredType(elementUtils.getTypeElement(ISemigroup.class.getTypeName()), params.get(0)), IRing.PROD_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createFapplySignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 2) {
            var input = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            var func = CompilerUtils.changeWildBy(typeUtils, iFace, typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), params.get(1)));
            var returnTyp = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(1));
            return new Signature(List.of(func, input), returnTyp, IApplicative.FAPPLY_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createLiftA2Signature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 3) {
            var input1 = typeUtils.getDeclaredType(elementUtils.getTypeElement(BiFunction.class.getTypeName()), params.get(0), params.get(1), params.get(2));
            var input2 = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            var input3 = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(1));
            var returnTyp = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(2));
            return new Signature(List.of(input1, input2, input3), returnTyp, IApplicative.LIFT_A2_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createDisjSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 1) {
            var input1 = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            return new Signature(List.of(input1, input1), input1, IAlternative.DISJ_NAME);
        } else {
            return Signature.invalid();
        }
    }


    private Signature createEmptySignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 1) {
            var input1 = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            return new Signature(List.of(), input1, IAlternative.EMPTY_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createJoinSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 1) {
            var input = CompilerUtils.changeWildBy(typeUtils, iFace, CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0)));
            var returnTyp = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            return new Signature(List.of(input), returnTyp, IMonad.JOIN_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createFlatMapSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 2) {
            var returnTyp = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(1));
            var input1 = typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), returnTyp);
            var input2 = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            return new Signature(List.of(input1, input2), returnTyp, IMonad.FLAT_MAP_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createOpSignature(DeclaredType iFace) {
        var params = iFace.getTypeArguments();
        if (!params.isEmpty()) {
            return new Signature(List.of(params.get(0), params.get(0)), params.get(0), ISemigroup.OP_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createUnitSignature(DeclaredType iFace) {
        var params = iFace.getTypeArguments();
        if (!params.isEmpty()) {
            return new Signature(List.of(), params.get(0), IMonoid.UNIT_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createFoldrSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 2) {
            var biFun = typeUtils.getDeclaredType(elementUtils.getTypeElement(BiFunction.class.getTypeName()), params.get(0), params.get(1), params.get(1));
            var foldableA = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            return new Signature(List.of(biFun, params.get(1), foldableA), params.get(1), IFoldable.FOLDR_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createFoldMapSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 2) {
            var fun = typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(0), params.get(1));
            var foldable = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            var iMonoid = typeUtils.getDeclaredType(elementUtils.getTypeElement(IMonoid.class.getTypeName()), params.get(1));
            return new Signature(List.of(iMonoid, fun, foldable), params.get(1), IFoldable.FOLD_MAP_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createTraverseSignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 2) {
            var fun = typeUtils.getDeclaredType(elementUtils.getTypeElement(Function.class.getTypeName()), params.get(1), params.get(0));
            var traversal = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(1));
            var iApplicative = typeUtils.getDeclaredType(elementUtils.getTypeElement(IApplicative.class.getTypeName()), params.get(0));
            return new Signature(List.of(iApplicative, fun, traversal), params.get(0), ITraversal.TRAVERSE_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private Signature createSequenceASignature(ExecutableType method, DeclaredType iFace) {
        var params = method.getTypeVariables();
        if (params.size() >= 1) {
            var traversal = CompilerUtils.changeWildBy(typeUtils, iFace, params.get(0));
            var iApplicative = typeUtils.getDeclaredType(elementUtils.getTypeElement(IApplicative.class.getTypeName()), params.get(0));
            return new Signature(List.of(iApplicative, traversal), params.get(0), ITraversal.SEQUENCE_A_NAME);
        } else {
            return Signature.invalid();
        }
    }

    private NecessaryMethods mapSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createMapSignature(x, iface), IFunctor.MAP_NAME);
    }

    private NecessaryMethods pureSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createPureSignature(x, iface), IApplicative.PURE_NAME);
    }

    private NecessaryMethods sumSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createSumSignature(x, iface), IRing.SUM_NAME);
    }

    private NecessaryMethods prodSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createProdSignature(x, iface), IRing.PROD_NAME);
    }

    private NecessaryMethods emptySignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createEmptySignature(x, iface), IAlternative.EMPTY_NAME);
    }

    private NecessaryMethods disjSignatureChecker(DeclaredType ifaca) {
        return new FunctionSignature(x -> createDisjSignature(x, ifaca), IAlternative.DISJ_NAME);
    }

    private NecessaryMethods fapplySignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFapplySignature(x, iface), IApplicative.FAPPLY_NAME);
    }

    private NecessaryMethods liftA2SignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createLiftA2Signature(x, iface), IApplicative.LIFT_A2_NAME);
    }

    private NecessaryMethods joinSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createJoinSignature(x, iface), IMonad.JOIN_NAME);
    }

    private NecessaryMethods flatMapSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFlatMapSignature(x, iface), IMonad.FLAT_MAP_NAME);
    }

    private NecessaryMethods opSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createOpSignature(iface), IMonoid.OP_NAME);
    }

    private NecessaryMethods unitSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createUnitSignature(iface), IMonoid.UNIT_NAME);
    }

    private NecessaryMethods foldrSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFoldrSignature(x, iface), IFoldable.FOLDR_NAME);
    }

    private NecessaryMethods foldMapSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFoldMapSignature(x, iface), IFoldable.FOLD_MAP_NAME);
    }

    private NecessaryMethods traverseSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createTraverseSignature(x, iface), ITraversal.TRAVERSE_NAME);
    }

    private NecessaryMethods sequenceASignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createSequenceASignature(x, iface), ITraversal.SEQUENCE_A_NAME);
    }

    /**
     * {@link NecessaryMethods} for an {@link IApplicative} to be correctly implemented
     *
     * @param iface type constructor inside {@link IApplicative}
     * @return A {@link NecessaryMethods} with requirements for an {@link IApplicative}
     */
    public NecessaryMethods applicativeSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(pureSignatureChecker(iface), new DisjNecessaryMethods(fapplySignatureChecker(iface), liftA2SignatureChecker(iface)));
    }


    /**
     * {@link NecessaryMethods} for an {@link IAlternative} to be correctly implemented
     *
     * @param iface type constructor inside {@link IAlternative}
     * @return A {@link NecessaryMethods} with requirements for an {@link IAlternative}
     */
    public NecessaryMethods alternativeSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(Set.of(disjSignatureChecker(iface), emptySignatureChecker(iface), new DisjNecessaryMethods(Set.of(applicativeSignatureChecker(iface), monadSignatureChecker(iface)))));
    }

    /**
     * {@link NecessaryMethods} for an {@link IRing} to be correctly implemented
     *
     * @param iface type constructor inside {@link IRing}
     * @return A {@link NecessaryMethods} with requirements for an {@link IRing}
     */
    public NecessaryMethods ringSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(sumSignatureChecker(iface), prodSignatureChecker(iface));
    }

    /**
     * {@link NecessaryMethods} for an {@link ITraversal} to be correctly implemented
     *
     * @param iface type constructor inside {@link ITraversal}
     * @return A {@link NecessaryMethods} with requirements for an {@link ITraversal}
     */
    public NecessaryMethods traversalSignatureChecker(DeclaredType iface) {
        return new DisjNecessaryMethods(traverseSignatureChecker(iface), sequenceASignatureChecker(iface));
    }

    /**
     * {@link NecessaryMethods} for an {@link IFunctor} to be correctly implemented
     *
     * @param iface type constructor inside {@link IFunctor}
     * @return A {@link NecessaryMethods} with requirements for an {@link IFunctor}
     */
    public NecessaryMethods functorSignatureChecker(DeclaredType iface) {
        return mapSignatureChecker(iface);
    }

    /**
     * {@link NecessaryMethods} for an {@link IMonad} to be correctly implemented
     *
     * @param iface type constructor inside {@link IMonad}
     * @return A {@link NecessaryMethods} with requirements for an {@link IMonad}
     */
    public NecessaryMethods monadSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(pureSignatureChecker(iface), new DisjNecessaryMethods(flatMapSignatureChecker(iface), new ConjNecessaryMethods(joinSignatureChecker(iface), new DisjNecessaryMethods(functorSignatureChecker(iface), applicativeSignatureChecker(iface)))));
    }

    /**
     * {@link NecessaryMethods} for an {@link ISemigroup} to be correctly implemented
     *
     * @param iface type inside {@link ISemigroup}
     * @return A {@link NecessaryMethods} with requirements for an {@link ISemigroup}
     */
    public NecessaryMethods semigroupSignatureChecker(DeclaredType iface) {
        return opSignatureChecker(iface);
    }

    /**
     * {@link NecessaryMethods} for an {@link IMonoid} to be correctly implemented
     *
     * @param iface type inside {@link IMonoid}
     * @return A {@link NecessaryMethods} with requirements for an {@link IMonoid}
     */
    public NecessaryMethods monoidSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(opSignatureChecker(iface), unitSignatureChecker(iface));
    }

    /**
     * {@link NecessaryMethods} for an {@link IFoldable} to be correctly implemented
     *
     * @param iface type constructor inside {@link IFoldable}
     * @return A {@link NecessaryMethods} with requirements for an {@link IFoldable}
     */
    public NecessaryMethods foldableSignatureChecker(DeclaredType iface) {
        return new DisjNecessaryMethods(foldrSignatureChecker(iface), foldMapSignatureChecker(iface));
    }

}
