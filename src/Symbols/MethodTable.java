package Symbols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodTable {
    public List<Type> params = new ArrayList<>();
    public Map<String, Type> locals = new HashMap<>();
    public Type returnType;
}
