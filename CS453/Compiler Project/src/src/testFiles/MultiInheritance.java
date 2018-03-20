class MultiInheritance {
    public static void main(String[] args) {
        One f;

        f = new Three();
        f = new Two();
        f = new One();
    }
}

class One {
    int a;
    public int foo() {
        int b;
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