package Symbols;

import java.util.HashMap;
import java.util.Map;

public class ClassTable {
    public Map<String, MethodTable> methods = new HashMap<>();
    public Map<String, String> fields = new HashMap<>();
    public String superClass;
    public String name;

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
            String t = fields.get(s);
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
