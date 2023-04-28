class Main {
    public static void main(String[] args) {
        System.out.println(new Loops().loops(5));
    }
}

class Loops {
    public int loops(int max) {
        int i;
        i = 0;
        while (i < max) {
            System.out.println(i);
            i = i + 1;
        }
        i = 0;

        return 0;
    }
}