package Symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodTable {
    public List<String> params = new ArrayList<>();
    public Map<String, String> locals = new HashMap<>();
    public String name;
    public String returnType;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  method ");
        sb.append(name);
        sb.append(":\n");
        sb.append("    params: ");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i));
            sb.append(", ");
        }

        sb.append("\n    returns: ").append(returnType);
        sb.append("\n    locals:\n");
        for (String s: locals.keySet()) {
            String t = locals.get(s);
            sb.append("      ");
            sb.append(t);
            sb.append(" ");
            sb.append(s);
            sb.append(";\n");
        }
        return sb.toString();
    }
}
