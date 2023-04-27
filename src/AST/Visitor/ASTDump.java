package AST.Visitor;

import AST.*;

public class ASTDump implements Visitor {
    private int currentDepth;

    private void indent() {
        for (int i = 0; i < currentDepth; i++) {
            System.out.print("  ");
        }
    }

    private void lineNumber(ASTNode node) {
        System.out.println(" (line " + node.line_number + ")");
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {
        // Unused
    }

    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        currentDepth = 1;
        System.out.println("Program");
        n.m.accept(this);
        for ( int i = 0; i < n.cl.size(); i++ ) {
            System.out.println();
            n.cl.get(i).accept(this);
        }
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        indent();
        System.out.print("MainClass " + n.i1);
        lineNumber(n);
        currentDepth++;
        n.s.accept(this);
        currentDepth--;
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        indent();
        System.out.print("Class " + n.i);
        lineNumber(n);

        currentDepth++;
        indent();
        System.out.println("fields:");
        currentDepth++;
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        currentDepth--;

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
        currentDepth--;
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        indent();
        System.out.print("Class " + n.i + " extends " + n.j);
        lineNumber(n);

        currentDepth++;
        indent();
        System.out.println("fields:");
        currentDepth++;
        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }
        currentDepth--;

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
        }
        currentDepth--;
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        indent();
        System.out.print("VarDecl ");
        n.t.accept(this);
        System.out.print(" " + n.i);
        lineNumber(n);
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
        indent();
        for (int i = 0; i < n.fl.size(); i++) {
            Formal f = n.fl.get(i);
            f.accept(this);
        }
        currentDepth--;
        indent();
        System.out.println("variables:");
        currentDepth++;
        indent();
        for (int i = 0; i < n.vl.size(); i++) {
            VarDecl varDecl = n.vl.get(i);
            n.vl.get(i).accept(this);
        }
        for (int i = 0; i < n.sl.size(); i++) {
            Statement s = n.sl.get(i);
            s.accept(this);
        }
        currentDepth--;
        indent();
        Exp e = n.e;
        System.out.print("Return ");
        lineNumber(e);
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
    }

    public void visit(IntArrayType n) {
        System.out.print("int []");
    }

    public void visit(BooleanType n) {
        System.out.print("boolean");
    }

    public void visit(IntegerType n) {
        System.out.print("int");
    }

    // String s;
    public void visit(IdentifierType n) {
        System.out.print(n.s);
    }

    // StatementList sl;
    public void visit(Block n) {
        for (int i = 0; i < n.sl.size(); i++) {
            Statement s = n.sl.get(i);
            s.accept(this);
        }
    }

    // Exp e;
    // Statement s1,s2;
    public void visit(If n) {
        indent();
        System.out.print("if " );
        lineNumber(n);
        indent();
        System.out.println("condition:");
        currentDepth++;
        n.e.accept(this);
        n.s1.accept(this);
        indent();
        currentDepth--;
        System.out.print("else");
        currentDepth++;
        n.s2.accept(this);
        currentDepth--;
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        indent();
        System.out.print("while ");
        lineNumber(n);
        indent();
        System.out.println("condition:");
        currentDepth++;
        n.e.accept(this);
        n.s.accept(this);
        currentDepth--;
    }

    // Exp e;
    public void visit(Print n) {
        indent();
        System.out.print("Print ");
        lineNumber(n);
        currentDepth++;
        n.e.accept(this);
        currentDepth--;
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        indent();
        System.out.print("= ");
        lineNumber(n);
        currentDepth++;
        n.i.accept(this);
        n.e.accept(this);
        currentDepth--;
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        indent();
        System.out.print("= ");
        lineNumber(n);
        n.i.accept(this);
        currentDepth++;
        System.out.println("index");
        n.e1.accept(this);
        n.e2.accept(this);
        currentDepth--;
    }

    // Exp e1,e2;
    public void visit(And n) {
        indent();
        System.out.print("And");
        lineNumber(n);
        currentDepth++;
        n.e1.accept(this);
        n.e2.accept(this);
        currentDepth--;
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        indent();
        System.out.print("LessThan");
        lineNumber(n);
        currentDepth++;
        n.e1.accept(this);
        n.e2.accept(this);
        currentDepth--;
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        indent();
        System.out.print("Plus");
        lineNumber(n);
        currentDepth++;
        n.e1.accept(this);
        n.e2.accept(this);
        currentDepth--;
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        indent();
        System.out.print("Minus");
        lineNumber(n);
        currentDepth++;
        n.e1.accept(this);
        n.e2.accept(this);
        currentDepth--;
    }

    // Exp e1,e2;
    public void visit(Times n) {
        indent();
        System.out.print("Times");
        lineNumber(n);
        currentDepth++;
        n.e1.accept(this);
        n.e2.accept(this);
        currentDepth--;
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        indent();
        System.out.println("ArrayLookup");
        lineNumber(n);

        currentDepth++;
        indent();
        System.out.println("on:");
        currentDepth++;
        n.e1.accept(this);
        currentDepth--;

        indent();
        System.out.println("at index:");
        currentDepth++;
        n.e2.accept(this);
        currentDepth -= 2;
    }

    // Exp e;
    public void visit(ArrayLength n) {
        indent();
        System.out.println("ArrayLength on:");
        n.e.accept(this);
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        indent();
        System.out.print("Call " + n.i.s);
        lineNumber(n);

        currentDepth++;
        indent();
        System.out.println("on class:");
        currentDepth++;
        n.e.accept(this);
        currentDepth--;
        indent();
        System.out.println("parameters:");

        currentDepth++;
        for ( int i = 0; i < n.el.size(); i++ ) {
            n.el.get(i).accept(this);
        }
        currentDepth -= 2;
    }

    // int i;
    public void visit(IntegerLiteral n) {
        indent();
        System.out.print("IntegerLiteral " + n.i);
        lineNumber(n);
    }

    public void visit(True n) {
        indent();
        System.out.print("True");
        lineNumber(n);
    }

    public void visit(False n) {
        indent();
        System.out.print("False");
        lineNumber(n);
    }

    // String s;
    public void visit(IdentifierExp n) {
        indent();
        System.out.print(n.s);
        lineNumber(n);
    }

    public void visit(This n) {
        indent();
        System.out.print("this");
        lineNumber(n);
    }

    // Exp e;
    public void visit(NewArray n) {
        indent();
        System.out.print("NewArray");
        lineNumber(n);
        currentDepth++;
        indent();
        System.out.println("size:");
        currentDepth++;
        n.e.accept(this);
        currentDepth -= 2;
    }

    // Identifier i;
    public void visit(NewObject n) {
        indent();
        System.out.print("NewObject " + n.i);
        lineNumber(n);
    }

    // Exp e;
    public void visit(Not n) {
        indent();
        System.out.print("Not");
        lineNumber(n);
        currentDepth++;
        n.e.accept(this);
        currentDepth--;
    }

    // String s;
    public void visit(Identifier n) {
        System.out.print(n.s);
    }
}
