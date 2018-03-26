class Array {
    public static void main(String[] args) {
        int[] a;
        int b;
        int c;

        b = 5;
        a = new int[10];
        c = 0;
        while (c < 5) {
            a[c] = ((c + 1) * 2) + 1;
            System.out.println(a[c]);
            c = c + 1;
        }
        System.out.println(a.length);
    }
}
