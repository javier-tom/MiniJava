package AST.Visitor;

import AST.*;

public class Codegen implements Visitor {
    private StringBuilder sb;

    public Codegen() {
        sb = new StringBuilder();
    }

    // Output an instruction
    private void insn(String i) {
        sb.append("\t").append(i).append('\n');
    }

    // Output a label
    private void label(String l) {
        sb.append(l).append(":\n");
    }

    private void directive(String d) {
        sb.append('\t').append(d).append('\n');
    }

    // Display added for toy example language.  Not used in regular MiniJava
    public void visit(Display n) {}


    // MainClass m;
    // ClassDeclList cl;
    public void visit(Program n) {
        n.m.accept(this);
        /*for ( int i = 0; i < n.cl.size(); i++ ) {
            n.cl.get(i).accept(this);
        }*/
        System.out.print(sb.toString());
    }

    // Identifier i1,i2;
    // Statement s;
    public void visit(MainClass n) {
        // create asm_main block
        directive(".text");
        directive(".globl asm_main");
        label("asm_main");
        insn("pushq %rbp");
        insn("movq %rsp,%rbp");

        n.s.accept(this);

        insn("movq %rbp,%rsp");
        insn("popq %rbp");
        insn("ret");
    }

    // Identifier i;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclSimple n) {}

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {}

    // Type t;
    // Identifier i;
    public void visit(VarDecl n) {}

    // Type t;
    // Identifier i;
    // FormalList fl;
    // VarDeclList vl;
    // StatementList sl;
    // Exp e;
    public void visit(MethodDecl n) {}

    // Type t;
    // Identifier i;
    public void visit(Formal n) {}

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
    public void visit(Print n) {
        n.e.accept(this);
        insn("movq %rax,%rdi");
        insn("call put");
    }

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
    public void visit(IntegerLiteral n) {
        insn("movq $" + n.i + ",%rax");
    }

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
