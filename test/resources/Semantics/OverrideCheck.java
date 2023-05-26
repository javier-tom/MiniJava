class Main {
    public static void main(String[] args) {
        System.out.println((new Override().run(10, 15)));
    }
}
class Override {
    int x;
    int y;
    boolean f1;
    boolean f2;

    public int run(int x, int y) {
        A a;
        B b;
        a = new A();
        b = new B();
        x = a.run(1,true);
        y = b.run(1,1);
        return 0;
    }
}

class A extends B {
    public int run(int x, boolean b) {
        return 2;
    }

    public C good() {
        return new C();
    }

    public F bad() {
        return new F();
    }
}

class B {
    public int run(int x, int b) {
        return 1;
    }

    public D good() {
        return new D();
    }

    public D bad() {
        return new D();
    }
}

class C extends D {}
class D extends F {}
class F {}