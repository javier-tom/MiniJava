class Main {
    public static void main(String[] args) {
        System.out.println((new Vars().run(10, 15)));
    }
}
class Vars {
    int a;
    int b;
    int c;
    boolean f1;
    boolean f2;

    public int run(int x, int y) { // x = 10, y = 15
        if (true < 1) { // true
            f1 = this.whiles(a, y);
        } else {
            if (false) {

            } else {

            }
        }
        return 1;
    }

    public boolean whiles(int a, int b) { // a = 0?, b = 15
        int[] arr;
        while (arr < b) {
            a = a + 1;
        }
        while (true){

        }
        while(1) {

        }
        arr = this.run3(a, b); // a = 15, b = 15
        return true;
    }

    public int[] run3(int a, int b) {
        int[] arr;
        arr = new int[a];
        a = a - b; // a = 0
        while(a < b - 10) { // ?
            arr[a] = 1;
            a = a + 1;
        }
        return arr;
    }
}