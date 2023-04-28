class Main {
    public static void main(String[] args) {
        System.out.println((new Thing()).run());
    }
}

class Thing {
    int x;
    boolean y;
    int[] z;

    public int init(int a, boolean b, int[] c) {
        x = a;
        y = b;
        z = c;
        return 0;
    }

    public int run() {
        return 0;
    }
}

class Foo extends Thing {
    int a;
    int b;

    public int run() {
        return Thing.init(a, true, new int[b]);
    }
}