package radin.backend.compilation;

import radin.core.chaining.ICompilerFunction;
import radin.core.errorhandling.AbstractCompilationError;
import radin.midanalysis.TypeAugmentedSemanticNode;
import radin.core.lexical.Token;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public abstract class AbstractCompiler implements ICompilerFunction<TypeAugmentedSemanticNode, Boolean> {

    
    private PrintWriter printWriter;
    
    public AbstractCompiler(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }
    
    
    @Override
    public Boolean invoke(TypeAugmentedSemanticNode input) {
        return compile(input);
    }
    
    abstract public boolean compile(TypeAugmentedSemanticNode node);
    
    @Override
    public List<AbstractCompilationError> getErrors() {
        return new LinkedList<>();
    }
    
    public String compileToString(TypeAugmentedSemanticNode node) {
        PrintWriter saved = printWriter; // temporarily change where output is directed
       
        StringWriter writer = new StringWriter();
        printWriter = new PrintWriter(writer);
        
        String output;
        if(compile(node))  {
            output = writer.toString();
        } else {
            output = null;
        }
        printWriter = saved;
        return output;
    }
    
    
    public void flush() {
        printWriter.flush();
    }
    
    public void close() {
        printWriter.close();
    }
    
    
    public void print(String s) {
        printWriter.print(s);
    }
    
    public void print(Token s){
        if(s.getImage() == null) printWriter.print(s.getType().toString());
        else printWriter.print(s.getImage());
    }
    
    public void print(Object obj) {
        printWriter.print(obj);
    }
    
    public void println() {
        printWriter.println();
    }
    
    public void println(String x) {
        printWriter.println(x);
    }
    
    public void println(Object x) {
        printWriter.println(x);
    }
    
    public PrintWriter getPrintWriter() {
        return printWriter;
    }
    
    protected void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }
    
    public PrintWriter printf(String format, Object... args) {
        return printWriter.printf(format, args);
    }
    
    public PrintWriter printf(Locale l, String format, Object... args) {
        return printWriter.printf(l, format, args);
    }
}
