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
    public int run(int x) {
        System.out.println(444444);
        return x;
    }
}

class B extends A {
    public int run(int x) {
        System.out.println(1111);
        return x;
    }
}

class C extends B {
    public int run(int x) {
        System.out.println(2222);
        return x;
    }
}

class D extends A {
    public int run(int x) {
        System.out.println(3333);
        return x;
    }
}