package radin.output.typeanalysis.errors;

import radin.core.errorhandling.AbstractCompilationError;
import radin.core.lexical.Token;
import radin.core.semantics.types.CXType;

import java.util.Arrays;

public class IncorrectTypeError extends AbstractCompilationError {
    public IncorrectTypeError(CXType expected, CXType gotten, Token expectedToken, Token gottenToken) {
        super("expected type: " + expected + " gotten type: " + gotten, Arrays.asList(expectedToken, gottenToken),
                expected.toString(), gotten.toString());
    }
    
    public IncorrectTypeError(CXType expected, CXType gotten, Token gottenToken) {
        super("expected type: " + expected + " gotten type: " + gotten, gottenToken,
                gotten.toString() + " is not " + expected.toString());
    }
}
