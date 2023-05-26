class Main {
    public static void main(String[] args) {
        System.out.println(new Array().run());
    }
}

class Array {
    public int run() {
        int[] arr;
        Seq seq;
        Deq deq;
        seq = new Seq();
        deq = new Deq();

        arr = new int[12345];
        arr = seq.seq(arr);
        arr = seq.seq(arr);
        arr = seq.dump(arr);
        seq = deq;
        arr = deq.swap(new int[5000]);
        arr = deq.swap(arr);
        arr = seq.seq(arr);
        arr = seq.dump(arr);
        
        return 5;
    }
}

class Seq {
    public int[] seq(int[] arr) {
        int i;
        i = 0;
        while (i < arr.length) {
            arr[i] = i;
            i = i + 1;
        }
        return arr;
    }

    public int[] dump(int[] arr) {
        int i;
        i = 0;
        while (i < arr.length) {
            System.out.println(arr[i]);
            i = i + 1;
        }
        return arr;
    }
}

class Deq extends Seq {
    int[] storage;

    public int[] swap(int[] arr) {
        int[] tmp;
        tmp = storage;
        storage = arr;
        return tmp;
    }

    public int[] seq(int[] arr) {
        int i;
        int j;
        i = 0;
        j = arr.length;
        while (i < arr.length) {
            arr[i] = j;
            i = i + 1;
            j = j - 1;
        }
        return arr;
    }
}