import java.io.File;

import Parser.sym;
import Scanner.*;
import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.util.Scanner;

public class MiniJava {

    public static void main (String[] args) {
        if (args.length != 3) {
            System.out.println("USAGE: MiniJava -S [filename]" );
            return;
        }

        if (args[1] != "-S") {
            System.out.println("USAGE: MiniJava -S [filename]");
            return;
        }

        try {
            scanner scanner = new scanner(new FileReader(args[2]));
            Symbol t = scanner.next_token();

            while (t.sym != sym.EOF) {
                System.out.println(scanner.symbolToString(t) + " ");
                t = scanner.next_token();
            }

            System.exit(0);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
