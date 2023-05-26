class Main {
    public static void main(String[] args) {
        System.out.println(new ArrayLength().run(312481));
    }
}

class ArrayLength {
    public int run(int len) {
        int[] arr;
        arr = new int[len];
        arr[0] = 213480120; // Array length is stored at the real arr[0]
        return arr.length;
    }
}
