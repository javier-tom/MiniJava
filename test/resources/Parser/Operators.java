class Main {
    public static void main(String[] args) {
        System.out.println(new Operators().Ops());
    }
}

class Operators {
    public int Ops() {
        int i;
        i = 0;
        i = i + 5;
        i = i * 3;
        i = i - 10;
        i = true && false;
        i = !true;
        i = true < false;
        return 0;
    }
}