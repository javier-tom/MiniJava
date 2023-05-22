package AST.Visitor;

import AST.*;
import Symbols.ClassTable;
import Symbols.MethodTable;
import Symbols.Type;

import java.util.Map;

public class TypeChecker implements Visitor {
    private Map<String, ClassTable> classes;
    private final Symbols.Type INT;
    private final Symbols.Type BOOL;
    private final Symbols.Type ARRAY;
    private final Symbols.Type ERR;

    private MethodTable scope;
    private ClassTable currClass;
    private int numErrors = 0;

    /**
     * Returns true if there hasn't been any errors in type checking
     */
    public boolean getStatus() {
        return numErrors == 0;
    }

    private void errorLine(ASTNode n, String desc) {
        System.err.println("Error on line " + n.line_number + ": " + desc);
        numErrors++;
    }

    private void expectType(Exp n, Type expected) {
        if (!expected.sameType(n.type)) {
            errorLine(n, "expected type " + expected + ". Got type " + n.type);
        }
    }

    private Type findVar(String name, ASTNode line_number) {
        Type t = scope.locals.get(name);
        if (t == null) {
            for (Type t1 : scope.params) {
                if (name.equals(t1.varName)) {
                    return t1;
                }
            }
        }
        if (t == null) {
            // Check in class hierarchy
            ClassTable tmp = currClass;
            while (tmp != null && t == null) {
                t = tmp.fields.get(name);
                tmp = classes.get(tmp.superClass);
            }
            if (t == null) {
                errorLine(line_number, "unknown variable " + name);
                return ERR;
            }
        }
        return t;
    }

    private void verifyExists(Type t, ASTNode line_number) {
        String name = t.toString(); // verifyExists overwrites the name on error
        if (!t.verifyExists()) {
            errorLine(line_number, "unknown type " + name);
        }
    }

    public TypeChecker(Map<String, ClassTable> classes) {
        this.classes = classes;
        INT = new Type(null, "int", classes);
        BOOL = new Type(null, "boolean", classes);
        ARRAY = new Type(null, "int[]", classes);
        ERR = new Type(null, "*error", classes);
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {}

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        // Typecheck return expression
        n.s.accept(this);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        currClass = classes.get(n.i.s);
        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        currClass = classes.get(n.i.s);
        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {}

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        scope = currClass.methods.get(n.i.s);
        // Check types for parameters and return, then check body
        verifyExists(scope.returnType, n);
        for (int i = 0; i < scope.params.size(); i++) {
            verifyExists(scope.params.get(i), n);
        }

        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.get(i).accept(this);
        }
        n.e.accept(this);
        if (!scope.returnType.isAssignable(n.e.type)) {
            errorLine(n.e, "expected type " + scope.returnType + ". Got type " + n.e.type);
        }
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {}

    public void visit(IntArrayType n) {}

    public void visit(BooleanType n) {}

    public void visit(IntegerType n) {}

    // String s;
    public void visit(IdentifierType n) {}

    // StatementList sl;
    public void visit(Block n) {
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.get(i).accept(this);
        }
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        n.e.accept(this);
        expectType(n.e, BOOL);
        n.s1.accept(this);
        n.s2.accept(this);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        n.e.accept(this);
        expectType(n.e, BOOL);
        n.s.accept(this);
    }

    // Exp e;
    public void visit(Print n) {
        n.e.accept(this);
        expectType(n.e, INT);
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        // Get type of variable
        Type t = findVar(n.i.s, n);
        n.e.accept(this);
        if (!t.isAssignable(n.e.type)) {
            errorLine(n, "cannot assign " + n.e.type + " to a " + t);
        }
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        n.e1.accept(this);
        expectType(n.e1, INT);
        n.e2.accept(this);
        expectType(n.e2, INT);

        Type t = findVar(n.i.s, n);
        if (!t.sameType(ARRAY)) {
            errorLine(n, "expected type " + ARRAY + ". Got type " + t);
        }
    }

    // Exp e1,e2;
    public void visit(And n) {
        n.e1.accept(this);
        n.e2.accept(this);
        expectType(n.e1, BOOL);
        expectType(n.e2, BOOL);
        n.type = BOOL;
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        n.e2.accept(this);
        expectType(n.e1, INT);
        expectType(n.e2, INT);
        n.type = BOOL;
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        n.e1.accept(this);
        n.e2.accept(this);
        expectType(n.e1, INT);
        expectType(n.e2, INT);
        n.type = INT;
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        n.e2.accept(this);
        expectType(n.e1, INT);
        expectType(n.e2, INT);
        n.type = INT;
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        n.e2.accept(this);
        expectType(n.e1, INT);
        expectType(n.e2, INT);
        n.type = INT;
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        n.e2.accept(this);
        expectType(n.e1, ARRAY);
        expectType(n.e2, INT);
        n.type = INT;
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
        expectType(n.e, ARRAY);
        n.type = INT;
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        // The type of that expression MUST be a class
        if (n.e.type == null) return;
        ClassTable ct = n.e.type.getClassTable();
        if (ct == null) {
            errorLine(n, n.e.type + " is not a class.");
            return;
        }

        MethodTable m = ct.methods.get(n.i.s);
        if (m == null) {
            errorLine(n, "method " + n.i.s + " does not exist.");
            n.type = ERR;
            return;
        }

        if (m.params.size() != n.el.size()) {
            errorLine(n, "expected " + m.params.size() +
                " arguments, got " + n.el.size() + " instead.");
            n.type = ERR;
            return;
        }

        // Typecheck parameters
        for (int i = 0; i < m.params.size(); i++) {
            Exp param = n.el.get(i);
            param.accept(this);
            if (!m.params.get(i).isAssignable(param.type)) {
                errorLine(n, "expected type " + m.params.get(i) + " in parameter " + (i + 1)
                    + " of the call to " + n.i.s + ". Got type " + param.type + " instead.");
            }
        }

        n.type = m.returnType;
    }

    // int i;
    public void visit(IntegerLiteral n) {
        n.type = INT;
    }

    public void visit(True n) {
        n.type = BOOL;
    }

    public void visit(False n) {
        n.type = BOOL;
    }

    // String s;
    public void visit(IdentifierExp n) {
        n.type = findVar(n.s, n);
    }

    public void visit(This n) {
        n.type = classes.get(scope.className).type;
    }

    // Exp e;
    public void visit(NewArray n) {
        n.e.accept(this);
        expectType(n.e, INT);
        n.type = ARRAY;
    }

    // Identifier i;
    public void visit(NewObject n) {
        // Make sure is an acutal class
        if (!classes.containsKey(n.i.s)) {
            errorLine(n, "unknown class " + n.i.s);
            n.type = ERR;
            return;
        }
        n.type = classes.get(n.i.s).type;
    }

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
        expectType(n.e, BOOL);
        n.type = BOOL;
    }

    // String s;
    public void visit(Identifier n) {}
}