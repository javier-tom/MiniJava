class Main {
    public static void main(String[] args) {
        System.out.println(new Operators().run());
    }
}

class Operators {
    public int run() {
        int[] arr;
        int five;
        int six;
        boolean b;
        int i;
        Stuff s;

        five = 5;
        b = true;
        six = 6;
        arr = new int[six];
        i = 0;
        s = new Stuff();

        System.out.println(five - six);
        b = s.x(five + six);
        b = s.x(five * six);

        while (i < six * five + arr[0] && !b) {
            arr[i] = i;
            i = i + 1;
        }

        return arr[six - five];
    }
}

class Stuff {
    public boolean x(int black_hole) {
        return false;
    }
}
