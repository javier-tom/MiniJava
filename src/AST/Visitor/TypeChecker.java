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
    public void visit(If n) {}

    // Exp e;
    // Statement s;
    public void visit(While n) {}

    // Exp e;
    public void visit(Print n) {}

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {}

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {}

    // Exp e1,e2;
    public void visit(And n) {}

    // Exp e1,e2;
    public void visit(LessThan n) {}

    // Exp e1,e2;
    public void visit(Plus n) {}

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        n.e2.accept(this);
        if (!n.e1.type.sameType(INT)) {
            System.err.println("Line " + n.line_number + ", expected type int. Got type " + n.e1.type);
        }
        if (!n.e2.type.sameType(INT)) {
            System.err.println("Line " + n.line_number + ", expected type int. Got type " + n.e2.type);
        }
        n.type = INT;
    }

    // Exp e1,e2;
    public void visit(Times n) {}

    // Exp e1,e2;
    public void visit(ArrayLookup n) {}

    // Exp e;
    public void visit(ArrayLength n) {}

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {}

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

    public void visit(This n) {}

    // Exp e;
    public void visit(NewArray n) {}

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