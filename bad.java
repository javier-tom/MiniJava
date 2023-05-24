class bad{
    public static void main(String[] a){
        System.out.println(new Base().run(5));
    }
}

class Base {
    int a;

    public int run(int num) {
        System.out.println(10);
        System.out.println(50 * 5);
        System.out.println(1);
        return 5;
    }
}

class sub {

}

class one extends Base {

}