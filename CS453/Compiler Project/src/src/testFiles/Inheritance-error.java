class Inheritance {
    public static void main(String[] args) {
        Foo_ f1;
        Foo_1 f2;
        Foo_2 f3;
        boolean a;
        boolean b;

        f2 = new Foo_1();
        a = f2.setBar1(2);
        b = f2.setBar(true);
        if (f2.getBar())
            System.out.println(f2.getBar1());
        else
            System.out.println(1);
    }
}

class Foo_ {
    boolean bar;
    boolean bar1;

    public boolean setBar(boolean val) {
        bar = val;
        return true;
    }

    public boolean getBar() {
        return bar;
    }
}

class Foo_1 extends Foo_ {
    int bar1;

    public boolean setBar1(int val) {
        bar1 = val;
        return true;
    }

    public int getBar1() {
        return bar1;
    }
}

class Foo_2 extends Foo_1 {
    public int setBar1(int val) {
        return 1;
    }
}