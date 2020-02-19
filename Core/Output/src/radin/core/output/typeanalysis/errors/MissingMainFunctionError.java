package radin.core.output.typeanalysis.errors;

public class MissingMainFunctionError extends Error {
    
    public MissingMainFunctionError() {
        super("No main function defined!");
    }
}
