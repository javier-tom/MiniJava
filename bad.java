class bad{
    public static void main(String[] a){
        {
            System.out.println(11 + 123);
            System.out.println(12 * 12);
            System.out.println(10 - 9);
        }
    }
}

class Base extends sub {
    int a;

    public int run() {
        a = 5;
        return a;
    }
}

class sub {

}

class one extends Base {

}