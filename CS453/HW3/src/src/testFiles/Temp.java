class MultiInheritance {
    public static void main(String[] args) {
        One f;
        int a;
        int b;
        int c;

//        f = new Three();
//        f = new Two();
//        f = new One();
        a = 1 * 2;
        b = a - 2;
        c = a + b;
        if (1 < 2)
            System.out.println(1);
        else
            System.out.println(0);
    }
}

class One {
    int a;
    public int foo(int tmp) {
        int[] b;
        b = new int[1]; // t.0 = 1
        a = 1; // [this + 4] = 1
        return 1;
    }
}

class Two extends One {
    public int bar() {
        return 2;
    }
}

class Three extends Two {
    public int foo() {
        return 3;
    }
}