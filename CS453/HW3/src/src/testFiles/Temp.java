class Temp {
    public static void main(String[] args) {
        One f;
        Two g;
        
        f = new Two();
        g = new Two();
        System.out.println(f.foo(10));
        System.out.println(g.bar(1));
        System.out.println(g.foo(10));
    }
}

class One {
    int a;
    public int foo(int tmp) {
        return tmp * 2;
    }

    public int bar(int tmp) {
        return tmp;
    }
}

class Two extends One {
    public int foo(int tmp) {
        return tmp;
    }
}
