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

    private MethodTable scope;

    private void expectType(Exp n, Type expected) {
        if (!n.type.sameType(expected)) {
            System.err.println("Error on line " + n.line_number + ": expected type "
                + expected + ". Got type " + n.type);
        }
    }

    public TypeChecker(Map<String, ClassTable> classes) {
        this.classes = classes;
        INT = new Type(null, "int", classes);
        BOOL = new Type(null, "boolean", classes);
        ARRAY = new Type(null, "int[]", classes);
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
        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
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
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.get(i).accept(this);
        }
        n.e.accept(this);
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
    public void visit(Print n) {}

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {}

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {}

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
        expectType(n.e, INT);
        n.type = INT;
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        n.e.accept(this);
        // The type of that expression MUST be a class
        ClassTable ct = n.e.type.getClassTable();
        if (ct == null) {
            System.err.print("Error on line " + n.line_number + ": ");
            System.err.print(n.e.type + " is not a class.");
            return;
        }

        MethodTable m = ct.methods.get(n.i.s);
        if (m.params.size() != n.el.size()) {
            System.err.print("Error on line " + n.line_number + ": ");
            System.err.print("expected " + m.params.size() + " arguments, ");
            System.err.println("got " + n.el.size() + " instead.");
            return;
        }

        // Typecheck parameters
        for (int i = 0; i < m.params.size(); i++) {
            Exp param = n.el.get(i);
            param.accept(this);
            if (!m.params.get(i).isAssignable(param.type)) {
                System.err.print("Error on line " + n.line_number + ": "
                    + "expected type " + m.params.get(i) + "in parameter " + (i + 1)
                    + " of the call to " + n.i.s + ". Got type " + param.type + " instead.");
            }
        }
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
    public void visit(IdentifierExp n) {}

    public void visit(This n) {
        n.type = classes.get(scope.className).type;
    }

    // Exp e;
    public void visit(NewArray n) {
        n.type = ARRAY;
    }

    // Identifier i;
    public void visit(NewObject n) {}

    // Exp e;
    public void visit(Not n) {
        n.e.accept(this);
        expectType(n.e, BOOL);
        n.type = BOOL;
    }

    // String s;
    public void visit(Identifier n) {}
}