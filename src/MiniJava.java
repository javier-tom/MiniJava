import java.io.*;

import AST.Program;
import AST.Visitor.ASTDump;
import AST.Visitor.Error;
import AST.Visitor.FillSymbolTables;
import AST.Visitor.PrettyPrintVisitor;
import AST.Visitor.TypeChecker;
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
        System.out.println("USAGE: MiniJava -[ASTP] [filename]");
        System.exit(1);
    }

    public static void main (String[] args) {
        Error.init();
        if (args.length != 2 || args[0].length() <= 1) {
            usage();
        }

        // Check arguments
        for (int i = 1; i < args[0].length(); i++) {
            char c = args[0].charAt(i);
            if (c != 'A' && c != 'S' && c != 'T' && c != 'P') {
                usage();
            }
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

        // The interesting part of the compiler. Scan, parse, typecheck, and compile
        // the program.
        int exitCode = 0;
        try {
            boolean valid = true;
            if (args[0].contains("S")) {
                Symbol t = scanner.next_token();

                while (t.sym != sym.EOF) {
                    System.out.print(scanner.symbolToString(t) + " ");
                    if (t.sym == sym.error) {
                        valid = false;
                    }
                    t = scanner.next_token();
                }

                // Scanner tests are all invalid programs, so exit now to avoid
                // the test cases failing from too many error messages
                System.exit(!valid ? 1 : 0);
            }
        } catch (IOException e) {
            // IOException so the System.exit doesn't get caught
            e.printStackTrace();
            System.exit(1);
        }

        try {
            // Keep going through the stages
            // Parse next
            parser p = new parser(scanner, sf);
            Program program = (Program)p.parse().value;
            if (args[0].contains("A")) {
                program.accept(new ASTDump());
            }
            if (args[0].contains("P")) {
                program.accept(new PrettyPrintVisitor());
            }

            // Now do semantics: fill symbol tables and verify them
            Map<String, ClassTable> classes = new HashMap<>();
            program.accept(new FillSymbolTables(classes));
            if (!checkSymbolTable(classes)) {
                exitCode = 1;
            }
            if (args[0].contains("T")) {
                // Dump symbol tables
                for (String s: classes.keySet()) {
                    if (classes.get(s) != null)
                        System.out.println(classes.get(s));
                }
            }
            
            // And now type check it
            TypeChecker tc = new TypeChecker(classes);
            program.accept(tc);
        } catch (Exception e) {
            e.printStackTrace();
            exitCode = 1;
        }
        if (!Error.getStatus()) exitCode = 1;
        System.exit(exitCode);
    }

    /**
     * Makes sure there are no dependency cycles in the symbol table, and
     * verifies the method overrides are correct
     * @param table the symbol table
     */
    private static boolean checkSymbolTable(Map<String, ClassTable> table) {
        boolean exitCode = true;
        Set<String> visited = new HashSet<>();
        for (Entry<String, ClassTable> e: table.entrySet()) {
            // Go up through the superclasses of each class, and make sure there
            // are no cycles
            ClassTable ct = e.getValue();
            if (ct == null) continue; // main class
            visited.clear();
            visited.add(e.getKey());
            visited.add("*error");
            String superClass = ct.superClass;
            while (superClass != null) {
                if (visited.contains(superClass)) {
                    // Cycle detected
                    ct.superClass = "*error";
                    System.err.println("Inheritance cycle detected for class " + e.getKey());
                    exitCode = false;
                    break;
                } else {
                    // Check any method overrides and copy superclass methods into
                    // this class table
                    ClassTable sup = table.get(superClass);
                    if (!checkOverrides(sup, ct)) {
                        exitCode = false;
                    }
                    visited.add(superClass);
                    superClass = table.get(superClass).superClass;
                }
            }
        }
        return exitCode;
    }

    /**
     * Fills method table in subclass with methods from superclass, and checks
     * overrides at the same time
     * @param sup superclass
     * @param sub subclass
     */
    private static boolean checkOverrides(ClassTable sup, ClassTable sub) {
        boolean exitCode = true;
        for (Entry<String, MethodTable> e: sup.methods.entrySet()) {
            if (sub.methods.containsKey(e.getKey())) {
                MethodTable aTable = e.getValue();
                MethodTable bTable = sub.methods.get(e.getKey());

                // Parameters and return type must match
                if (!aTable.returnType.sameType(bTable.returnType)
                    || aTable.params.size() != bTable.params.size()) {
                    System.err.println("Method " + e.getKey() + " is not a valid override "
                        + "in class " + sub.name);
                    exitCode = false;
                    continue;
                }

                for (int i = 0; i < aTable.params.size(); i++) {
                    if (!aTable.params.get(i).sameType(bTable.params.get(i))) {
                        System.err.println("Method " + e.getKey() + " is not a valid override "
                        + "in class " + sub.name);
                        exitCode = false;
                        break;
                    }
                }
            } else {
                sub.methods.put(e.getKey(), e.getValue());
            }
        }
        return exitCode;
    }
}
