class Runner {
    public static void main (String[] args) {
        System.out.println((new VarDec()).Meth(true));
    }
}

class VarDec {
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