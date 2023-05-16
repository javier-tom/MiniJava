package Symbols;

import AST.BooleanType;
import AST.IdentifierType;
import AST.IntArrayType;
import AST.IntegerType;

import java.util.HashMap;
import java.util.Map;

public class Type {
    //String className;
    public String varName;
    private String type;
    private Map<String, ClassTable> classes;

    public Type(String varName, String type, Map<String,ClassTable> classes) {
        this.varName = varName;
        this.type = type;
        this.classes = classes;
    }

    public boolean isAssignable(Type t) {
        if (type.equals(t.type)) return true;
        if (classes.containsKey(type) && classes.containsKey(t.type)) {
            ClassTable classTable = classes.get(t.type);
            while (classTable.superClass != null) {
                if (type.equals(classTable.superClass)) return true;
                classTable = classes.get(classTable.superClass);
            }
        }
        return false;
    }

    public String toString() {
        return type;
    }

}
