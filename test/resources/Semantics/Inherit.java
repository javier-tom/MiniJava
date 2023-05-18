class Main {
    public static void main(String[] args) {
        System.out.println(new A().run());
    }
}

class B {
    public int run() {
        return 17;
    }
}

class A extends B {
    public int whatever(int stuff) {
        return stuff;
    }
}

