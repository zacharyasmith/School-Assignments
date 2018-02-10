public class Parse {
    public static void main(String[] args) throws Exception {
        boolean debug = false;
        try {
            TokenScanner ts = new TokenScanner(debug);
            Expr result = new Expr(ts, debug);
            if (result.expr() && ts.eof())
                System.out.println("Expression parsed successfully");
            else
                System.out.println("Invalid expression");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
