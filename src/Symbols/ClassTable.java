package Symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassTable {
    public Map<String, MethodTable> methods = new HashMap<>();
    public List<Type> fields = new ArrayList<>();
}
