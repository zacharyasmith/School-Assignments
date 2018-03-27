class Temp {
    public static void main(String[] args) {
        One a;
        One b;
        Two c;

        a = new One();
        System.out.println(a.foo());

        b = new Two();
        System.out.println(b.foo());

        c = new Two();
        System.out.println(c.bar());
        System.out.println(c.foo());
    }
}

class One {
    boolean b;
    int a;
    public int foo() {
        a = 1;
        return a;
    }
}

class Two extends One {
    public int bar() {
        a = 2;
        return a;
    }
    public int foo() {
        a = 3;
        return 2 * a;
    }
}
