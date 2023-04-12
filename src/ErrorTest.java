public class ErrorTest {
    /*
    This is a scanner test with some errors
     */

    public static void main(String[] args) {
        // missing semi-colon
        int i = 0

        // unsupported increment
        while (i < 10) {
            i += 1;
        }

        // unsupported division
        int j = i / 5;
        int k = i - 3;

        // if-else statement
        if (i < j) {
            i = i + k;
        } else {
            i = i - k;
        }
        // SOUT statement
        System.out.println(i);

        /* this is a multi line comment inside a program!!!
        Don't expect text!
         */

        // missing LPAREN in statement
        if ((i < j) && i < k)) {
            System.out.println(i);
        }
    }

}
