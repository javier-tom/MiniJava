class Main {
    public static void main(String[] args) {
        System.out.println((new Thing()).run());
    }
}

class Thing {
    int[] arr;
    public int run() {
        int[] a;
        int i;

        i = 0;
        a = new int[10];
        arr = new int [10];
        a[0] = 0;

        while (i < a.length) {
            a[i] = i;
            i = i + 1;
        }

        i = i - 1;
        while (0-1 < i) {
            arr[i] = a[i];
            i = i - 1;
        }
        return i;
    }
}