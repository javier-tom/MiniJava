import java.io.*;

import AST.Program;
import AST.Visitor.ASTDump;
import AST.Visitor.PrettyPrintVisitor;
import Parser.parser;
import Parser.sym;
import Scanner.*;
import Symbols.ClassTable;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;

import java.util.HashMap;
import java.util.Map;

public class MiniJava {
    // Symbol tables
    private Map<String, ClassTable> classes = new HashMap<>();

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
        } else {
            usage();
        }
        System.exit(0);
    }
}
