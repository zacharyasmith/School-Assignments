class VarDec {
    public static void main (String[] args) {
        System.out.println((new Class()).Meth(true));
    }
}

class Class {
    int var;
    public int Meth(boolean success) {
        boolean failure;

        failure = !success;
        if (success)
            var = 0;
        else
            var = 1;
        return var;
    }
}