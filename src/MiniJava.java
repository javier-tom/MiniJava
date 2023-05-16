import java.io.*;

import AST.Program;
import AST.Visitor.ASTDump;
import AST.Visitor.FillSymbolTables;
import AST.Visitor.PrettyPrintVisitor;
import Parser.parser;
import Parser.sym;
import Scanner.*;
import Symbols.ClassTable;
import Symbols.MethodTable;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public class MiniJava {
    private static void usage() {
        System.out.println("USAGE: MiniJava -S [filename]");
        System.exit(1);
    }

    public static void main (String[] args) {
        if (args.length != 2) {
            usage();
        }

        FileReader f = null;
        try {
            f = new FileReader(args[1]);
        } catch(Exception e) {
            System.exit(1);
        }
        ComplexSymbolFactory sf = new ComplexSymbolFactory();
        Reader in = new BufferedReader(f);
        scanner scanner = new scanner(in, sf);

        if (args[0].equals("-S")) {
            // Scanner
            // System.exit doesn't work right in a try, so workaround
            int exitCode = 0;
            boolean valid = true;
            try {
                Symbol t = scanner.next_token();

                while (t.sym != sym.EOF) {
                    System.out.print(scanner.symbolToString(t) + " ");
                    if (t.sym == sym.error) {
                        valid = false;
                    }
                    t = scanner.next_token();
                }
            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace();
                exitCode = 1;
            }
            if (!valid) exitCode = 1;
            System.exit(exitCode);
        } else if (args[0].equals("-A")) {
            parser p = new parser(scanner, sf);
            try {
                Symbol sym = p.parse();
                Program program = (Program)sym.value;
                program.accept(new ASTDump());
            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace();
                System.exit(1);
            }
        } else if (args[0].equals("-P")) {
            parser p = new parser(scanner, sf);
            try {
                Symbol sym = p.parse();
                Program program = (Program)sym.value;
                program.accept(new PrettyPrintVisitor());
            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace();
                System.exit(1);
            }
        } else if (args[0].equals("-T")) {
            // Fill symbol tables
            Map<String, ClassTable> classes = new HashMap<>();
            parser p = new parser(scanner, sf);
            try {
                Symbol sym = p.parse();
                Program program = (Program)sym.value;
                program.accept(new FillSymbolTables(classes));
                checkSymbolTable(classes);
            } catch (Exception e) {
                System.err.println(e);
                e.printStackTrace();
                System.exit(1);
            }

            // Dump symbol tables
            for (String s: classes.keySet()) {
                if (classes.get(s) != null)
                    System.out.println(classes.get(s));
            }
        } else {
            usage();
        }
        System.exit(0);
    }

    /**
     * Makes sure there are no dependency cycles in the symbol table, and
     * verifies the method overrides are correct
     * @param table the symbol table
     */
    private static void checkSymbolTable(Map<String, ClassTable> table) {
        Set<String> visited = new HashSet<>();
        for (Entry<String, ClassTable> e: table.entrySet()) {
            // Go up through the superclasses of each class, and make sure there
            // are no cycles
            ClassTable ct = e.getValue();
            visited.clear();
            visited.add(e.getKey());
            String superClass = e.getValue().superClass;
            while (superClass != null) {
                if (visited.contains(superClass)) {
                    // Cycle detected
                    ct.superClass = "*error";
                    System.err.println("Inheritance cycle detected for class " + e.getKey());
                    break;
                } else {
                    // Check any method overrides and copy superclass methods into
                    // this class table
                    ClassTable sup = table.get(superClass);
                    checkOverrides(sup, ct);
                    visited.add(superClass);
                    superClass = table.get(superClass).superClass;
                }
            }
        }
    }

    /**
     * Fills method table in subclass with methods from superclass, and checks
     * overrides at the same time
     * @param sup superclass
     * @param sub subclass
     */
    private static void checkOverrides(ClassTable sup, ClassTable sub) {
        for (Entry<String, MethodTable> e: sup.methods.entrySet()) {
            if (sub.methods.containsKey(e.getKey())) {
                MethodTable aTable = e.getValue();
                MethodTable bTable = sub.methods.get(e.getKey());

                // Parameters and return type must match
                if (!aTable.returnType.sameType(bTable.returnType)
                    || aTable.params.size() != bTable.params.size()) {
                    System.err.println("Method " + e.getKey() + " is not a valid override "
                        + "in class " + sub.name);
                    continue;
                }

                for (int i = 0; i < aTable.params.size(); i++) {
                    if (!aTable.params.get(i).sameType(bTable.params.get(i))) {
                        System.err.println("Method " + e.getKey() + " is not a valid override "
                        + "in class " + sub.name);
                        break;
                    }
                }
            } else {
                sub.methods.put(e.getKey(), e.getValue());
            }
        }
    }
}
