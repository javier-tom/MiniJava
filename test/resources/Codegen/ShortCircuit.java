class Main {
    public static void main(String[] args) {
        System.out.println(new ShortCircuit().run(5));
    }
}

class ShortCircuit {
    public int run(int x) {
        // Should print 1
        if (x < 10 && this.print(1)) {} else {}
        // Should not print anything
        if (10 < x && this.print(2)) {} else {}
        return 5;
    }

    public boolean print(int x) {
        System.out.println(x);
        return true;
    }
}
