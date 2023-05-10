package Symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodTable {
    public List<Type> params = new ArrayList<>();
    public Map<String, Type> locals = new HashMap<>();
    public Type returnType;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    params: ");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i));
            sb.append(", ");
        }

        sb.append("\n    returns: ").append(returnType);
        sb.append("\n    locals:\n");
        for (String s: locals.keySet()) {
            Type t = locals.get(s);
            sb.append("      ");
            sb.append(t);
            sb.append(" ");
            sb.append(s);
            sb.append(";\n");
        }
        return sb.toString();
    }
}
