class Main {
    public static void main(String[] args) {
        System.out.println(new If().If(5));
    }
}

class If {
    public int If(int max) {
        int i;
        i = 0;
        if (i < max) {
            System.out.println(i);
        } else {
            System.out.println(max);
        }
        return 0;
    }
}