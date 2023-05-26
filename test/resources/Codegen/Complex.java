class Main {
    public static void main(String[] args) {
        System.out.println(new Over().run(5));
    }
}

class Over {
    public int run(int x) {
        C c;
        c = new C();
        System.out.println(c.putVal());
        return 0;
    }
}

class A {
    public int doMath(int x) {
        int a;
        int b;
        a = x;
        b = a * 10;
        b = b - a;
        return b;
    }
}

class B extends A {
    A a;
    int sum;

    public int checkSum(boolean b) {
        a = new A();
        if (b) {
            sum = a.doMath(1);
        } else {
            sum = a.doMath(1) + a.doMath(2);
        }
        return sum;
    }
}

class C extends B {
    int[] arr;
    B b;

    public int putVal() {
        int sum;
        b = new B();
        sum = b.checkSum(true);
        arr = new int[sum];
        while (0 < sum) {
            sum = sum - 1;
            arr[sum] = b.doMath(sum);
        }
        return arr[arr.length - 1];
    }
}