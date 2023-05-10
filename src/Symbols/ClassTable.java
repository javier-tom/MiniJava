package Symbols;

import java.util.HashMap;
import java.util.Map;

public class ClassTable {
    public Map<String, MethodTable> methods = new HashMap<>();
    public Map<String, Type> fields = new HashMap<>();
}
