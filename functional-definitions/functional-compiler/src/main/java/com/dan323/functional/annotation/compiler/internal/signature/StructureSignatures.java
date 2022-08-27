package com.dan323.functional.annotation.compiler.internal.signature;

import com.dan323.functional.annotation.algs.IMonoid;
import com.dan323.functional.annotation.algs.ISemigroup;
import com.dan323.functional.annotation.compiler.internal.CompilerUtils;
import com.dan323.functional.annotation.funcs.IApplicative;
import com.dan323.functional.annotation.funcs.IFoldable;
import com.dan323.functional.annotation.funcs.IFunctor;
import com.dan323.functional.annotation.funcs.IMonad;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    public Signature createFoldMapSignature(ExecutableType method, DeclaredType iFace) {
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

    private NecessaryMethods mapSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createMapSignature(x, iface));
    }

    private NecessaryMethods pureSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createPureSignature(x, iface));
    }

    private NecessaryMethods fapplySignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFapplySignature(x, iface));
    }

    private NecessaryMethods liftA2SignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createLiftA2Signature(x, iface));
    }

    private NecessaryMethods joinSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createJoinSignature(x, iface));
    }

    private NecessaryMethods flatMapSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFlatMapSignature(x, iface));
    }

    private NecessaryMethods opSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createOpSignature(iface));
    }

    private NecessaryMethods unitSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createUnitSignature(iface));
    }

    private NecessaryMethods foldrSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFoldrSignature(x, iface));
    }

    private NecessaryMethods foldMapSignatureChecker(DeclaredType iface) {
        return new FunctionSignature(x -> createFoldMapSignature(x, iface));
    }

    public NecessaryMethods applicativeSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(pureSignatureChecker(iface), new DisjNecessaryMethods(fapplySignatureChecker(iface), liftA2SignatureChecker(iface)));
    }

    public NecessaryMethods functorSignatureChecker(DeclaredType iface) {
        return mapSignatureChecker(iface);
    }

    public NecessaryMethods monadSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(pureSignatureChecker(iface), new DisjNecessaryMethods(flatMapSignatureChecker(iface), new ConjNecessaryMethods(joinSignatureChecker(iface), new DisjNecessaryMethods(functorSignatureChecker(iface), applicativeSignatureChecker(iface)))));
    }

    public NecessaryMethods semigroupSignatureChecker(DeclaredType iface) {
        return opSignatureChecker(iface);
    }

    public NecessaryMethods monoidSignatureChecker(DeclaredType iface) {
        return new ConjNecessaryMethods(opSignatureChecker(iface), unitSignatureChecker(iface));
    }

    public NecessaryMethods foldableSignatureChecker(DeclaredType iface) {
        return new DisjNecessaryMethods(foldrSignatureChecker(iface), foldMapSignatureChecker(iface));
    }

}
