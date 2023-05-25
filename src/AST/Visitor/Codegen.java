package AST.Visitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import AST.*;
import Symbols.ClassTable;
import Symbols.MethodTable;
import java_cup.runtime.Symbol;

public class Codegen implements Visitor {
    private StringBuilder sb;
    private int numPush;
    private Map<String, ClassTable> classes;
    private ClassTable currClass;
    private MethodTable currMethod;
    private int currLabel;

    private String[] paramRegist = {"rdi", "rsi", "rdx", "rcx", "r8", "r9"};

    public Codegen(Map<String, ClassTable> classes) {
        sb = new StringBuilder();
        this.classes = classes;
        numPush = 0;
        currLabel = 0;
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

    private void push(String reg) {
        insn("pushq %"+reg);
        numPush++;
    }

    private void pop(String reg) {
        insn("popq %"+reg);
        numPush--;
    }

    private void align() {
        if (numPush % 2 != 0) {
            insn("pushq %rax");
        }
    }

    private void unalign() {
        if (numPush % 2 != 0) {
            insn("addq $8, %rsp");
        }
    }

    private String getMem(String s) {
        for (int i = 0; i < currMethod.params.size(); i++) {
            if (s.equals(currMethod.params.get(i).varName)) {
                return "-"+(i + 2)*8 + "(%rbp)";
            }
        }
        Symbols.Type t = currMethod.locals.get(s);
        if (t != null) {
            return "-"+(t.location + currMethod.params.size() + 2)*8 + "(%rbp)";
        }
        return "ERROR!";
        // loop through fields
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
    public void visit(ClassDeclSimple n) {
        // Set up vtable
        directive(".data");
        label(n.i.s + "$$");
        insn(".quad 0");
        currClass = classes.get(n.i.s);
        List<MethodTable> methods = new ArrayList<>(currClass.methods.values());
        methods.sort((MethodTable a, MethodTable b) -> a.vtableIdx - b.vtableIdx);

        for (MethodTable m: methods) {
            insn(".quad " + n.i.s + '$' + m.name);
        }

        directive(".text");
        for (int i = 0; i < n.ml.size(); i++) {
            n.ml.get(i).accept(this);
        }
    }

    // Identifier i;
    // Identifier j;
    // VarDeclList vl;
    // MethodDeclList ml;
    public void visit(ClassDeclExtends n) {
        directive(".data");
        label(n.i.s + "$$");
        insn(".quad "+n.j.s+ "$$");

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
        // figure out locals for stack
        label(currClass.name + '$' + n.i.s);
        insn("pushq %rbp");
        insn("movq %rsp,%rbp");
        
        currMethod = currClass.methods.get(n.i.s);
        for (int i = 0; i < currMethod.params.size() + 1; i++) {
            push(paramRegist[i]);
        }

        insn("xorq %rax, %rax");
        for (int i = 0; i < currMethod.locals.size(); i++) {
            insn("pushq %rax"); // Zero initialze all locals
        }

        // Generate code for all statements
        for (int i = 0; i < n.sl.size(); i++) {
            n.sl.get(i).accept(this);
        }

        // Leaves result in %rax, and then we can just return
        n.e.accept(this);
        insn("movq %rbp,%rsp");
        insn("popq %rbp");
        insn("ret");
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
        String elseBegin = "else" + (currLabel++);
        String endBlock = "endElse" + (currLabel++);

        n.e.accept(this);
        insn("testq %rax, %rax");
        insn("jz " + elseBegin);
        n.s1.accept(this);
        insn("jmp " + endBlock);
        label(elseBegin);
        n.s2.accept(this);
        label(endBlock);
    }

    // Exp e;
    // Statement s;
    public void visit(While n) {
        String whileBegin = "while" + (currLabel++);
        String whileEnd = "endWhile" + (currLabel++);

        insn("jmp " + whileEnd);
        label(whileBegin);
        n.s.accept(this);
        label(whileEnd);

        n.e.accept(this);
        insn("testq %rax, %rax");
        insn("jnz " + whileBegin);
    }

    // Exp e;
    public void visit(Print n) {
        n.e.accept(this);
        insn("movq %rax,%rdi");
        align();
        insn("call put");
        unalign();
    }

    // Identifier i;
    // Exp e;
    public void visit(Assign n) {
        String s = getMem(n.i.s);
        n.e.accept(this);
        insn("movq %rax,"+s);
    }

    // Identifier i;
    // Exp e1,e2;
    public void visit(ArrayAssign n) {
        String s = getMem(n.i.s);
        n.e1.accept(this);
        push("rax");
        n.e2.accept(this);
        // do bound check

        pop("rdx");
        insn("movq "+s+",%rcx");
        insn("movq %rax, 8(%rcx, %rdx, 8)");
    }

    // Exp e1,e2;
    public void visit(And n) {
        n.e1.accept(this);
        push("rax");
        n.e2.accept(this);
        pop("rdi");
        insn("andq %rdi, %rax");
    }

    // Exp e1,e2;
    public void visit(LessThan n) {
        n.e1.accept(this);
        push("rax");
        n.e2.accept(this);
        pop("rdi");
        insn("cmpq %rax, %rdi");
        insn("setl %al");
        insn("movzbq %al, %rax");
    }

    // Exp e1,e2;
    public void visit(Plus n) {
        n.e1.accept(this);
        push("rax");
        n.e2.accept(this);
        pop("rdi");
        insn("addq %rdi, %rax");
    }

    // Exp e1,e2;
    public void visit(Minus n) {
        n.e1.accept(this);
        push("rax");
        n.e2.accept(this);
        pop("rdi");
        insn("subq %rax, %rdi");
        insn("movq %rdi, %rax");
    }

    // Exp e1,e2;
    public void visit(Times n) {
        n.e1.accept(this);
        push("rax");
        n.e2.accept(this);
        pop("rdi");
        insn("imulq %rdi");
    }

    // Exp e1,e2;
    public void visit(ArrayLookup n) {
        n.e1.accept(this);
        push("rax");
        n.e2.accept(this);
        pop("rdi");
        insn("movq 8(%rdi, %rax, 8), %rax");
    }

    // Exp e;
    public void visit(ArrayLength n) {
        n.e.accept(this);
        insn("movq (%rax), %rax");
    }

    // Exp e;
    // Identifier i;
    // ExpList el;
    public void visit(Call n) {
        // evaluate parameters and then call
        n.e.accept(this);
        push("rax");
        for (int i = 0; i < n.el.size(); i++) {
            n.el.get(i).accept(this);
            push("rax");
        }

        // Fill up registers for method call
        for (int i = n.el.size(); i >= 0; i--) {
            pop(paramRegist[i]);
        }

        ClassTable c = classes.get(n.e.type.toString());
        MethodTable m = c.methods.get(n.i.s);
        insn("movq (%rdi), %rax"); // get vtable location
        align();
        insn("call *" + ((m.vtableIdx + 1) * 8) + "(%rax)");
        unalign();
    }

    // int i;
    public void visit(IntegerLiteral n) {
        insn("movq $" + n.i + ",%rax");
    }

    public void visit(True n) {
        insn("movq $1, %rax");
    }

    public void visit(False n) {
        insn("xorq %rax, %rax");
    }

    // String s;
    public void visit(IdentifierExp n) {
        insn("movq " + getMem(n.s) + ",%rax");
    }

    public void visit(This n) {
        insn("movq -8(%rbp), %rax");
    }

    // Exp e;
    public void visit(NewArray n) {
        n.e.accept(this);
        insn("leaq 8(, %rax, 8), %rdi");
        align();
        insn("call mjcalloc");
        unalign();
    }

    // Identifier i;
    public void visit(NewObject n) {
        ClassTable c = classes.get(n.i.s);
        int size = (1 + c.fields.size()) * 8; // For vtable pointer
        insn("movq $" + size + ", %rdi");
        align();
        insn("call mjcalloc");
        unalign();

        // fill in vtable pointer
        insn("leaq " + n.i.s + "$$(%rip), %rdi");
        insn("movq %rdi, (%rax)");
    }

    // Exp e;
    public void visit(Not n) {
        insn("xorq $1, %rax");
    }

    // String s;
    public void visit(Identifier n) {}
}
