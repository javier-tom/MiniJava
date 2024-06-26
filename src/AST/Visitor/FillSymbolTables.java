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
    public void visit(Display n) {}

    private String getType(Type t) {
        if (t instanceof IntegerType) return "int";
        else if (t instanceof IntArrayType) return "int[]";
        else if (t instanceof BooleanType) return "boolean";
        else if (t instanceof IdentifierType) return ((IdentifierType) t).s;
        else return "*error";
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
        // It's the first thing, so no need to check for duplicates
        classes.put(n.i1.s, null);
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {
        if (classes.containsKey(n.i.s)) {
            Error.errorLine(n, "duplicate class " + n.i.s + " declared");
            return;
        }
        currClass = new ClassTable();
        currClass.name = n.i.s;

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
            if (currMethod != null) {
                // Method might be a duplicate, and it will be null in that case
                currMethod.vtableIdx = i;
                currMethod = null;
            }
        }

        currClass.type = new Symbols.Type(null, n.i.s, classes);
        classes.put(n.i.s, currClass);
        currClass = null;
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        if (classes.containsKey(n.i.s)) {
            Error.errorLine(n, "duplicate class " + n.i.s + " declared");
            return;
        }
        currClass = new ClassTable();
        currClass.name = n.i.s;
        currClass.superClass = n.j.s;

        // Will get calculated later. Can't do now, since we might not have visited
        // the super class yet.
        currClass.superFields = -1;

        for ( int i = 0; i < n.vl.size(); i++ ) {
            n.vl.get(i).accept(this);
        }

        for ( int i = 0; i < n.ml.size(); i++ ) {
            n.ml.get(i).accept(this);
            if (currMethod != null) {
                // Method might be a duplicate, and it will be null in that case
                currMethod.vtableIdx = i;
                currMethod = null;
            }
        }

        currClass.type = new Symbols.Type(null, n.i.s, classes);
        classes.put(n.i.s, currClass);
        currClass = null;
    }

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {
        String s = getType(n.t);
        Symbols.Type t = new Symbols.Type(n.i.s, s, classes);
        if (currMethod != null) {
            if (currMethod.locals.containsKey(n.i.s)) {
                Error.errorLine(n, "duplicate variable " + n.i.s + " delcared");
                return;
            }
            t.location = currMethod.locals.size();
            currMethod.locals.put(n.i.s, t);
        } else {
            if (currClass.fields.containsKey(n.i.s)) {
                Error.errorLine(n, "duplicate field " + n.i.s + " delcared");
                return;
            }
            t.location = currClass.fields.size();
            currClass.fields.put(n.i.s, t);
        }
    }

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {
        if (currClass.methods.containsKey(n.i.s)) {
            Error.errorLine(n, "duplicate method " + n.i.s + " declared");
            return;
        }
        currMethod = new MethodTable();
        currMethod.name = n.i.s;

        for (int i = 0; i < n.fl.size(); i++) {
            n.fl.get(i).accept(this);
        }

        for (int i = 0; i < n.vl.size(); i++) {
            n.vl.get(i).accept(this);
        }

        currMethod.returnType = new Symbols.Type(null, getType(n.t), classes);
        currMethod.className = currClass.name;
        currClass.methods.put(n.i.s, currMethod);
    }

    // Type t;
    // Identifier i;
    public void visit(Formal n) {
        String s = getType(n.t);
        Symbols.Type t = new Symbols.Type(n.i.s, s, classes);
        currMethod.params.add(t);
    }

    public void visit(IntArrayType n) {}

    public void visit(BooleanType n) {}

    public void visit(IntegerType n) {}

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
