class Main {
    public static void main(String[] args) {
        System.out.println(new Over().run(5));
    }
}

class Over {
    public int run(int x) {
        A a;
        B b;
        C c;
        D d;
        a = new D();
        b = new B();
        c = new C();
        d = new D();
        System.out.println(a.run(0));
        System.out.println(b.run(1));
        System.out.println(c.run(2));
        System.out.println(d.run(3));
        return x;
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
        b.checkSum(true);
        sum = b.sum;
        arr = new int[sum];
        while (0 < sum) {
            sum = sum - 1;
            arr[sum] = b.a.doMath(sum);
        }
        return arr[arr.length - 1];
    }
}