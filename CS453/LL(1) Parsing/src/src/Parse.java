public class Parse {
    private boolean debug;

    public Parse() throws Exception {
        this.debug = true;
        TokenScanner ts = new TokenScanner(debug);
        Expr result = new Expr(ts, debug);
        System.out.println(result.expr() && ts.eof());
    }

    public static void main(String[] args) throws Exception {
        Parse p = new Parse();
    }
}
