package Symbols;

import java.util.HashMap;
import java.util.Map;

public class ClassTable {
    public Map<String, MethodTable> methods = new HashMap<>();
    public Map<String, Type> fields = new HashMap<>();
    public int superFields = 0; // Number of fields on super class
    public String superClass;
    public String name;
    public Type type;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ");
        sb.append(name);
        sb.append(":\n");
        sb.append("  super: ");
        sb.append(superClass);
        sb.append("\n");
        sb.append("  fields:\n");
        for (String s: fields.keySet()) {
            Type t = fields.get(s);
            sb.append("    ");
            sb.append(t);
            sb.append(" ");
            sb.append(s);
            sb.append(";\n");
        }

        for (String s: methods.keySet()) {
            sb.append(methods.get(s).toString());
        }
        return sb.toString();
    }
}
