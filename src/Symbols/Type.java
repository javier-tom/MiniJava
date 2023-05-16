package Symbols;

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

    /**
     * Return true iff the underlying type is the same
     */
    public boolean sameType(Type o) {
        return o.type.equals(this.type);
    }

    public String toString() {
        return type;
    }

    public ClassTable getClassTable() {
        return classes.get(this.type);
    }
}
