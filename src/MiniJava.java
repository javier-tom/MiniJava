import java.io.File;

import Parser.sym;
import Scanner.*;
import java_cup.runtime.Symbol;

import java.io.FileReader;
import java.util.Scanner;

public class MiniJava {

    public static void main (String[] args) {
        if (args.length != 2) {
            System.out.println("USAGE: MiniJava -S [filename]" );
            System.exit(1);
            return;
        }

        if (!args[0].equals("-S")) {
            System.out.println("USAGE: MiniJava -S [filename]");
            System.exit(1);
            return;
        }

        // System.exit doesn't work right in a try, so workaround
        int exitCode = 0;
        boolean valid = true;
        try {
            scanner scanner = new scanner(new FileReader(args[1]));
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
    }
}
