class Main {
    public static void main(String[] args) {
        System.out.println(new LogicOperators().run());
    }
}

class LogicOperators {
    boolean z;

    public int run() {
        boolean x;
        boolean y;

        x = false;
        y = true;
        z = x && y;

        if (x) {
            System.out.println(0);
        } else {
            x = true;
        }

        if (y && z) {
            System.out.println(1);
        } else {
            z = true;
        }
        
        if (!!!!!(y && z && x)) {
            System.out.println(1238459123);
        } else {
            System.out.println(4328914);
        }

        x = false;
        if (!!!!!!!!!!!!!!!!!!!!!x && y) {
            System.out.println(0-123498132);
        } else {
            y = false;
        }

        if (x && !!!!!!!!!!!!!!!!!!!!!!!y) {
            System.out.println(0-0-5);
        } else {
            System.out.println(2);
        }
        return 5;
    }
}
