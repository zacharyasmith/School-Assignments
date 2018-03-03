class Foo {
    public static void main(String[] args) {
        One f;

        f = new Three();
        f = new Two();
        f = new One();
    }
}

class One {

}

class Two extends One {

}

class Three extends Two {

}