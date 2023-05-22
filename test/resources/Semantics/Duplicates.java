class Main {
    public static void main(String[] args) {
        System.out.println(new B().run());
    }
}

// Duplicate of main class, definitely should cause error
class Main {
    int x;
}

class A {}

class A {
    // Duplicate, and should be reported as error
}

class B {
    public int run() {
        return 6;
    }

    // Duplicate, should cause error
    public int run() {
        return 7;
    }
}

class C {
    public int stuff() {
        int x;
        int x;
        x = 5;
        return x;
    }
}

class D {
    int x;
    int x;
}