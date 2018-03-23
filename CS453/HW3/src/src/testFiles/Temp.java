class MultiInheritance {
    public static void main(String[] args) {
        One f;
        int a;

        f = new Three();
        f = new Two();
        f = new One();
        a = 5;
        System.out.println(5);
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