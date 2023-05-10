package AST.Visitor;

import AST.*;
import Symbols.ClassTable;
import Symbols.MethodTable;

import java.util.Map;

public class FillSymbolTables implements Visitor {
    private Map<String, ClassTable> classes;
    private ClassTable currClass = null;
    private MethodTable currMethod = null;

    public FillSymbolTables(Map<String, ClassTable> classes) {
        this.classes = classes;
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {
        // Unused
    }

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

        n.s.accept(this);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        currClass = new ClassTable();

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }

        classes.put(n.i.s, currClass);
        currClass = null;
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        currClass = new ClassTable();

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }

        classes.put(n.i.s, currClass);
        currClass = null;
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        n.t.accept(this);
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        indent();
        System.out.print("MethodDecl " + n.i.toString());
        lineNumber(n);
        currentDepth++;
        indent();
        System.out.print("returns ");
        n.t.accept(this);
        System.out.println();
        indent();
        System.out.println("params:");
        currentDepth++;
        for (int i = 0; i < n.fl.size(); i++) {
            Formal f = n.fl.get(i);
            f.accept(this);
        }
        currentDepth--;
        indent();
        System.out.println("variables:");
        currentDepth++;
        for (int i = 0; i < n.vl.size(); i++) {
            VarDecl varDecl = n.vl.get(i);
            n.vl.get(i).accept(this);
        }
        currentDepth--;
        for (int i = 0; i < n.sl.size(); i++) {
            Statement s = n.sl.get(i);
            s.accept(this);
        }
        indent();
        Exp e = n.e;
        System.out.println("Return");
        currentDepth++;
        e.accept(this);
        currentDepth = 1;
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        indent();
        n.t.accept(this);
        System.out.print(" ");
        n.i.accept(this);
        System.out.println();
    }

    public void visit(IntArrayType n) {
        System.out.print("int[]");
    }

    public void visit(BooleanType n) {
        System.out.print("boolean");
    }

    public void visit(IntegerType n) {
        System.out.print("int");
    }

    // String s;
    public void visit(IdentifierType n) {}

    // StatementList sl;
    public void visit(Block n) {}

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
    public void visit(Minus n) {}

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
    public void visit(IntegerLiteral n) {}

    public void visit(True n) {}

    public void visit(False n) {}

    // String s;
    public void visit(IdentifierExp n) {}

    public void visit(This n) {}

    // Exp e;
    public void visit(NewArray n) {}

    // Identifier i;
    public void visit(NewObject n) {}

    // Exp e;
    public void visit(Not n) {}

    // String s;
    public void visit(Identifier n) {}
}
