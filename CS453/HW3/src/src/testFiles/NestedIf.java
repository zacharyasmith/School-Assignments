class NestedIf {
    public static void main (String[] args) {
        boolean a;

        a = false;
        if (!a) {
            System.out.println(1);
            if (!a) {
                System.out.println(2);
                if (!a) {
                    System.out.println(3);
                    while (!a) {
                        System.out.println(99);
                        a = true;
                    }
                } else {
                    System.out.println(4);
                }
            } else {
                System.out.println(5);
            }
        } else {
            System.out.println(6);
        }
    }
}
