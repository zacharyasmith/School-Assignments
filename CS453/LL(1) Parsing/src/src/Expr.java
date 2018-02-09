import java.util.regex.Pattern;

public class Expr {
    final static String sp = "| ";
    private boolean debug;
    private TokenScanner ts;
    private Stack stack;
    
    public Expr(TokenScanner ts, boolean debug) {
        this.debug = debug;
        this.ts = ts;
        this.stack = new Stack();
    }

    public boolean expr() {
        return expr("");
    }
    
    public boolean expr(String ind) {
        if (debug) System.out.println( ind + "expr: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (a(ind + sp))
            return t(ind + sp);
        else
            return false;
    }

    public boolean t(String ind) {
        if (debug) System.out.println( ind + "t: checking " + ts.peek());
        if (ts.eof())
            return true;
        expr(ind + sp);
        return true;
    }

    public boolean a(String ind) {
        if (debug) System.out.println( ind + "a: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (b(ind + sp))
            return u(ind + sp);
        return false;
    }

    public boolean u(String ind) {
        if (debug) System.out.println( ind + "u: checking " + ts.peek());
        if (ts.eof())
            return true;
        if (binop(ind + sp))
            return a(ind + sp);
        return true;
    }

    public boolean b(String ind) {
        if (debug) System.out.println( ind + "b: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (incrop(ind + sp))
            return b(ind + sp);
        return c(ind + sp);
    }

    public boolean c(String ind) {
        if (debug) System.out.println( ind + "c: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (d(ind + sp))
            return v(ind + sp);
        return false;
    }

    public boolean v(String ind) {
        if (debug) System.out.println( ind + "v: checking " + ts.peek());
        if (ts.eof())
            return true;
        if (incrop(ind + sp))
            return v(ind + sp);
        return true;
    }

    public boolean d(String ind) {
        if (debug) System.out.println( ind + "d: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (is(ts, "$", debug, ind + sp))
            return b(ind + sp);
        return e(ind + sp);
    }

    public boolean e(String ind) {
        if (debug) System.out.println( ind + "e: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (is(ts, "(", debug, ind + sp)) {
            if (expr(ind + sp))
                return is(ts, ")", debug, ind + sp);
            return false;
        }
        return num(ind + sp);
    }

    public boolean incrop(String ind) {
        if (debug) System.out.println( ind + "incrop: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (ts.peek().equals("++") | ts.peek().equals("--")) {
            if (debug) System.out.println(ind + "consuming token");
            ts.consume();
            return true;
        }
        return false;
    }

    public boolean binop(String ind) {
        if (debug) System.out.println( ind + "binop: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (ts.peek().equals("+") | ts.peek().equals("-")) {
            if (debug) System.out.println(ind + "consuming token");
            ts.consume();
            return true;
        }
        return false;
    }

    public boolean num(String ind) {
        if (debug) System.out.println( ind + "num: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (Pattern.matches("[0-9]", ts.peek())) {
            if (debug) System.out.println(ind + "consuming token");
            ts.consume();
            return true;
        }
        return false;
    }

    public boolean is(TokenScanner ts, String s, boolean debug, String ind) {
        if (debug) System.out.println( ind + "is: checking " + ts.peek() + " against " + s);
        if (ts.eof())
            return false;
        if (s.equals(ts.peek())) {
            if (debug) System.out.println(ind + "consuming token");
            ts.consume();
            return true;
        }
        return false;
    }
}
