class Main {
    public static void main(String[] args) {
        System.out.println(new Array().run());
    }
}

class Array {
    public int run() {
        int[] arr;
        int x;
        arr = new int[5];
        x = 5;
        System.out.println(arr[x]);
        x = x - 6;
        System.out.println(arr[x]);
        return 5;
    }
}