package Symbols;

import java.util.Map;

public class Type {
    //String className;
    public String varName;
    private String type;
    private Map<String, ClassTable> classes;
    public int location;

    public Type(String varName, String type, Map<String,ClassTable> classes) {
        this.varName = varName;
        this.type = type;
        this.classes = classes;
        this.location = Integer.MIN_VALUE;
    }

    public boolean isAssignable(Type t) {
        if (type.equals("*error") || t.type.equals("*error")) {
            // This is to silence repeated errors about a non-existent type.
            return true;
        }
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

    /**
     * Figure out if this type actually exists somewhere, or convert it to an error type if not
     * @return True iff exists, false if it gets turned into an error
     */
    public boolean verifyExists() {
        if (type == "int" || type == "int[]" || type == "boolean"
            || classes.containsKey(type))
            return true;
        else {
            varName = null;
            type = "*error";
            return false;
        }
    }

    public String toString() {
        return type;
    }

    public ClassTable getClassTable() {
        return classes.get(this.type);
    }
}
