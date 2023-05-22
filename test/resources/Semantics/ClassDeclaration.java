class Main {
    public static void main(String[] args) {
        System.out.println((new ClassDecl().run(10, 15)));
    }
}
class ClassDecl {
    int x;
    int y;
    boolean f1;
    boolean f2;

    public int run(int x, int y) {
        A a;
        a = new A();
        return 0;
    }
}

class A {
}

class B {
}

class C extends A {

}

class C extends B {

}