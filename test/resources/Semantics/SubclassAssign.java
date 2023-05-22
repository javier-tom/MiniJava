class Main {
    public static void main(String[] args) {
        System.out.println(new SubclassAssign().run());
    }
}

class SubclassAssign {
    public int run() {
        A a;
        B b;
        B bad;
        int x;
        MethodParamTest t;

        a = new A();
        b = new B();
        t = new MethodParamTest();
        a = b;
        bad = a;

        // Now test it in method parameters
        x = t.testA(a);
        x = t.testB(b);
        x = t.testA(b); // should be valid
        x = t.testB(a); // should error out
        return x;
    }
}

class MethodParamTest {
    public int testA(A a) {
        return 5;
    }

    public int testB(B b) {
        return 5;
    }
}

class A {}

class B extends A {}
