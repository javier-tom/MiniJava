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
        int b;
        int c;
        a = 11;
        b = a;
        arr = new int[x];
        c = x + y;

        b = arr.length;
        arr[arr.length - 1] = 777777;

        f = x < y;

        return arr[arr.length - 1];
    }
}