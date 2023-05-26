class Main {
    public static void main(String[] args) {
        System.out.println(new A().run(5, 10));
    }
}

class A {
    int a;
    int[] arr;
    boolean f;

    public int run(int x, int y) {
        f = false;
        arr = new int[x];

        System.out.println(111111);

        if (x < y) {
            System.out.println(x);
        } else {
            System.out.println(y);
        }

        System.out.println(111111);

        if (!f) {
            System.out.println(0);
        } else {
            System.out.println(1);
        }

        System.out.println(111111);

        while (0 < x) {
            x = x - 1;
            arr[x] = (x + 1) * 8;
            System.out.println(arr[x]);
        }

        System.out.println(111111);

        while (x < y && y < x) {
            System.out.println(9999999);
        }
        return 0;
    }
}