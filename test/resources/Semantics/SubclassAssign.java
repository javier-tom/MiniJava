class Main {
    public static void main(String[] args) {
        System.out.println(new SubclassAssign().run());
    }
}

class SubclassAssign {
    public int run() {
        A a;
        B b;
        a = new A();
        b = new B();
        a = b;
        b = a;
        return 5;
    }
}

class A extends B {}

class B {}
