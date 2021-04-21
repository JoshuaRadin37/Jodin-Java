package radin.midanalysis;

import radin.core.errorhandling.AbstractCompilationError;
import radin.core.lexical.Token;
import radin.core.semantics.AbstractSyntaxNode;
import radin.core.semantics.TypeEnvironment;
import radin.core.semantics.generics.*;
import radin.core.semantics.types.CXIdentifier;
import radin.core.semantics.types.CXType;
import radin.core.semantics.types.compound.AbstractCXClassType;
import radin.core.semantics.types.compound.CXClassType;
import radin.core.semantics.types.compound.CXFunctionPointer;
import radin.core.utility.Pair;
import radin.midanalysis.typeanalysis.VariableTypeTracker;
import radin.output.tags.GenericLocationTag;

import java.util.*;

public class GenericModule implements IScopedTracker<Object>{
    
    // TODO: Add support for Generic Classes
    
    public static class GenericRedeclarationError extends AbstractCompilationError {
        public GenericRedeclarationError(Token newDec, List<CXParameterizedClassType> attempt,
                                         CXGenericFunction original) {
            super("Attempted to declare a generic function with same type sigature of an existing funciton",
                    Arrays.asList(original.getDeclarationToken(), newDec),
                    "Parameter types size = " + original.getParameterTypes().size(),
                    "is the same as " + attempt.size());
        }
    }
    
    public static class IncorrectParameterTypeCountError extends AbstractCompilationError {
        public IncorrectParameterTypeCountError(int found, int expected, Token caller) {
            super("Incorrect amount of parameter types", caller, "Found: " + found + "  Expected: " + expected);
        }
        
        public IncorrectParameterTypeCountError(int found, Token caller) {
            super("No corresponding function with this amount of parameter types", caller,
                    "Found: " + found);
        }
    }
    
    public static class IllegalParameterTypesError extends AbstractCompilationError {
        public IllegalParameterTypesError(List<CXType> found, List<CXParameterizedClassType> expected, Token caller) {
            super("Expected Parameter types: " + found, caller, "Found:" + found);
        }
    }
    
    
    
    private HashMap<Pair<CXIdentifier, Integer>, CXGenericFunction> genericFunctionsDictionary = new HashMap<>();
    private HashMap<CXIdentifier, CXGenericClassFactory> genericClassHashMap = new HashMap<>();
    private HashMap<ICXGenericFactory<?>, GenericLocationTag> locationTagHashMap = new HashMap<>();
    
    
    /**
     * Creates a generic function
     * @param name The fully qualified name of the function
     * @param returnType The return type of the function
     * @param parameterizedTypes The parameter types
     * @param argsTypes This is the types of the arguments for the function
     * @param relevantBody the body of the function, can be null if not available at declaration
     * @param e The Type Environemnt
     * @param decToken Can't be null. This refers to the first declaration token
     */
    public void declareGenericFunction(CXIdentifier name,
                                       CXType returnType,
                                       List<CXParameterizedClassType> parameterizedTypes,
                                       List<CXType> argsTypes,
                                       AbstractSyntaxNode relevantBody,
                                       TypeEnvironment e,
                                       Token decToken,
                                       GenericLocationTag tag) {
        
        Pair<CXIdentifier, Integer> pair = new Pair<>(name, parameterizedTypes.size());
        if(genericFunctionsDictionary.containsKey(
                pair
        )) {
            throw new GenericRedeclarationError(decToken, parameterizedTypes, genericFunctionsDictionary.get(pair));
        }
        
        CXGenericFunction genericFunction = new CXGenericFunction(
                name,
                returnType,
                argsTypes,
                e,
                parameterizedTypes,
                relevantBody,
                decToken
        );
        genericFunctionsDictionary.putIfAbsent(pair, genericFunction);
        locationTagHashMap.put(genericFunction, tag);
    }
    
    public void declareGenericClass(AbstractSyntaxNode classBody, List<CXParameterizedClassType> parameterizedTypes) {
        //if(genericClassHashMap.containsKey())
    }
    
    public <T> GenericLocationTag genericLocationTag(ICXGenericFactory<T> generic) {
        return locationTagHashMap.get(generic);
    }
    
    public Collection<CXGenericFunction> getRegisteredGenericFunctions() {
        return genericFunctionsDictionary.values();
    }
    
    public GenericInstance<CXFunctionPointer> genericFunctionCallOn(CXIdentifier identifier, List<CXType> parameterTypes) {
        
        var pair = new Pair<>(identifier, parameterTypes.size());
        if(!genericFunctionsDictionary.containsKey(pair)) {
            throw new IncorrectParameterTypeCountError(parameterTypes.size(), identifier.getCorresponding());
            
        }
        
        CXGenericFunction genericFunction = genericFunctionsDictionary.get(pair);
        if(!genericFunction.typesValid(parameterTypes)) {
            throw new IllegalParameterTypesError(parameterTypes, genericFunction.getParameterizedTypes(),
                    identifier.getCorresponding());
        }
        
        return genericFunction.createInstance(parameterTypes);
    }
    
    public GenericInstance<AbstractCXClassType> createType(CXIdentifier type, List<CXType> parameterTypes) {
    
    }
    
    @Override
    public void typeTrackingClosure() {
    
    }
    
    @Override
    public void typeTrackingClosure(CXClassType classType) {
    
    }
    
    @Override
    public void typeTrackingClosureLoad(CXClassType cxClassType) {
    
    }
    
    @Override
    public void setTrackerStack(Stack<Object> trackerStack) {
    
    }
    
    @Override
    public VariableTypeTracker getCurrentTracker() {
        return null;
    }
    
    @Override
    public boolean isBaseTracker() {
        return false;
    }
    
    @Override
    public void releaseTrackingClosure() {
    
    }
    
    @Override
    public void reset() {
        genericFunctionsDictionary.clear();
        locationTagHashMap.clear();
    }
}
