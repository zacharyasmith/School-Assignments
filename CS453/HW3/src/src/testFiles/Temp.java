class Temp {
    public static void main(String[] args) {
        One a;
        One b;
        Two c;
        int d;

        a = new One();
        System.out.println(a.foo(10));
        b = new Two();
        System.out.println(b.foo(100));
        b = new One();
        System.out.println(b.foo(100));
        c = new Two();
        System.out.println(c.bar(1000));
        System.out.println(c.foo(1000));
    }
}

class One {
    public int foo(int tmp) {
        return 200;
    }

    public int bar(int tmp) {
        return 347;
    }
}

class Two extends One {
    public int foo(int tmp) {
        return this.bar(2 * tmp);
    }
}
