package radin.core.semantics.types.compound;

import radin.core.semantics.TypeEnvironment;
import radin.core.semantics.types.CXIdentifier;
import radin.core.semantics.types.CXType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public abstract class CXCompoundType extends CXType implements ICXCompoundType {
    
    private List<FieldDeclaration> fields;
    private CXIdentifier typeName;
    private boolean anonymous;
    
    
    
    public CXCompoundType(List<FieldDeclaration> fields) {
        this.fields = fields;
        anonymous = true;
    }
    
    public CXCompoundType(FieldDeclaration f1, FieldDeclaration... fields) {
        this.fields = Arrays.asList(fields);
        this.fields.add(0, f1);
        anonymous = true;
    }
    
    public CXCompoundType(CXIdentifier identifier, List<FieldDeclaration> fields) {
        this.fields = fields;
        this.typeName = identifier;
        anonymous = false;
    }
    
    public CXCompoundType(CXIdentifier identifier, FieldDeclaration f1, FieldDeclaration... fields) {
        this.fields = Arrays.asList(fields);
        this.fields.add(0, f1);
        this.typeName = identifier;
        anonymous = false;
    }
    
    
    @Override
    public boolean isPrimitive() {
        return false;
    }
    
    
    @Override
    public List<FieldDeclaration> getFields() {
        return fields;
    }
    
    public List<FieldDeclaration> getAllFields() {
        return fields;
    }
    
    
    @Override
    public String getTypeName() {
        return typeName.toString();
    }
    
    @Override
    public String getCTypeName() {
        return typeName.generateCDefinition();
    }
    
    @Override
    public CXIdentifier getTypeNameIdentifier() {
        return typeName;
    }
    
    public boolean isAnonymous() {
        return anonymous;
    }
    
    @Override
    public boolean isValid(TypeEnvironment e) {
        for (FieldDeclaration field : fields) {
            if(!field.getType().isValid(e)) return false;
        }
        return true;
    }
    
    @Override
    public long getDataSize(TypeEnvironment e) {
        long sum = 0;
        for (FieldDeclaration field : fields) {
            sum += field.getType().getDataSize(e);
        }
        return sum;
    }
    
    @Override
    public String toString() {
        return super.toString().replaceAll("\\s+", " ");
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        CXCompoundType that = (CXCompoundType) o;
        
        if (anonymous != that.anonymous) return false;
        return Objects.equals(typeName, that.typeName);
    }
    
    @Override
    public int hashCode() {
        int result = typeName != null ? typeName.hashCode() : 0;
        result = 31 * result + (anonymous ? 1 : 0);
        return result;
    }
    
    
    /**
     * Creates a modified version of the C Declaration that matches the pattern {@code \W+}
     *
     * @return Such a string
     */
    @Override
    public String getSafeTypeString() {
        return typeName.toString().replaceAll("\\W+", "_");
    }
}
