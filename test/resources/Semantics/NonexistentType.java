class Main {
    public static void main(String[] args) {
        System.out.println(new A().run());
    }
}

class A {
    public int run() {
        return 17;
    }

    public Foo stuff() {
        return 5;
    }

    public int bad(Bad b) {
        return 17;
    }
}
