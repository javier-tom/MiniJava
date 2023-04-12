public class NoErrorTest {
    /*
    This is a scanner test with no errors
     */

    public static void main(String[] args) {
        // assignment
        int i = 0;
        // while loop
        while (i < 10) {
            i = i + 1;
        }
        // other operators
        int j = i * 5;
        int k = i - 3;

        // if-else statement
        if (i < j) {
            i = i + k;
        } else {
            i = i - k;
        }
        // SOUT statement
        System.out.println(i);

        // Strings
        String s = "i + k = 17";

        System.out.println(s);

        /* this is a multi line comment inside a program!!!
        Don't expect text!
         */

        // AND statement with LPAREN and RPAREN
        if ((i < j) && (i > k)) {
            System.out.println("Use of AND");
        }
    }

}
