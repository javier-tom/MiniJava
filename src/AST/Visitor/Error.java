package AST.Visitor;

import AST.ASTNode;

// Helper class for logging error messages
public class Error {
    private static int numErrors = 0;
    // Stop printing errors after getting this many
    private static final int CUTOFF = 50;

    public static void errorLine(ASTNode n, String desc) {
        errorLine(n.line_number, desc);
    }

    public static void errorLine(int line_number, String desc) {
        error("Error on line " + line_number + ": " + desc);
    }

    // Return true iff no errors have been logged
    public static boolean getStatus() {
        return numErrors == 0;
    }

    // MUST call this at start of program! static fields don't get reinitialized during tests
    public static void init() {
        numErrors = 0;
    }

    private static void error(String desc) {
        if (numErrors < CUTOFF) {
            System.err.println(desc);
        }
        numErrors++;
    }

}
