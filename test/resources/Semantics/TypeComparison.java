class Main {
    public static void main(String[] args) {
        System.out.println((new TypeCompare().run(10, 15)));
    }
}
class TypeCompare {
    int a;
    int b;
    int c;
    int[] arr;
    boolean flag;

    public int run(int a, int b) {
        a = 8; // precedence, should not change global a
        c = b; // should be parameter value
        arr = a + c; // not type compatible
        flag = 1; // not type compatible
        c = this.secondRun(a, flag); // parameter list not same type
        c = a + b;
        return flag; // error, return type does not match
    }

    public int secondRun(int x, int y) {
        a = x;
        System.out.println(a); // 8
        System.out.println(flag);
        arr = new int[flag]; // not type compatible
        return c;
    }
}