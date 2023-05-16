class bad{
    public static void main(String[] a){
	System.out.println(new Base());
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