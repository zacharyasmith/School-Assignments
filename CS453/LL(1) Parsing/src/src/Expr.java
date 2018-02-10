import java.util.ArrayList;
import java.util.regex.Pattern;

public class Expr {
    final static String sp = "| ";
    private boolean debug;
    private TokenScanner ts;
    private Stack stack;

    public Expr(TokenScanner ts, boolean debug) {
        this.debug = debug;
        this.ts = ts;
        stack = new Stack(debug);
    }

    public boolean expr() {
        boolean result = expr("");
        if (result && ts.eof())
            System.out.println(" " + stack.pop());
        return result;
    }

    private boolean expr(String ind) {
        if (debug) System.out.println(ind + "expr: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (a(ind + sp)) {
            int ptr = stack.getPointer();
            if (t(ind + sp)) {
                if (ptr < stack.getPointer()) {
                    if (debug) System.out.println(ind + "concatenation");
                    String t = stack.pop();
                    String a = stack.pop();
                    stack.push(a + " " + t + " _");
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean t(String ind) {
        if (debug) System.out.println(ind + "t: checking " + ts.peek());
        if (ts.eof())
            return true;
        while (a(ind + sp)) {
            String right = stack.pop();
            String left = stack.pop();
            stack.push(left + " " + right + " _");
        }
//        expr(ind + sp);
        return true;
    }

    private boolean a(String ind) {
        if (debug) System.out.println(ind + "a: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (b(ind + sp)) {
            int ptr = stack.getPointer();
            if (u(ind + sp)) {
                if (ptr < stack.getPointer()) {
                    if (debug) System.out.println(ind + "binop");
                    String a = stack.pop();
                    String binop = stack.pop();
                    String b = stack.pop();
                    stack.push(b + " " + a + " " + binop);
                }
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean u(String ind) {
        if (debug) System.out.println(ind + "u: checking " + ts.peek());
        if (ts.eof())
            return true;
        while (binop(ind + sp)) {
            if (!b(ind + sp))
                return false;
            String right = stack.pop();
            String binop = stack.pop();
            String left = stack.pop();
            stack.push(left + " " + right + " " + binop);
        }
        return true;
    }

    private boolean b(String ind) {
        if (debug) System.out.println(ind + "b: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (incrop(ind + sp)) {
            if (b(ind + sp)) {
                if (debug) System.out.println(ind + "pre-inc");
                String b = stack.pop(); // b
                stack.push(b + " " + stack.pop() + "_"); // incrop
                return true;
            }
            return false;
        }
        return c(ind + sp);
    }

    private boolean c(String ind) {
        if (debug) System.out.println(ind + "c: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (d(ind + sp)) {
            int ptr = stack.getPointer();
            // is last $?
            if (!stack.peek(1).equals("$")) {
                if (v(ind + sp)) {
                    if (ptr < stack.getPointer()) {
                        if (debug) System.out.println(ind + "post-inc c");
                        String v = stack.pop();
                        String d = stack.pop();
                        stack.push(d + " " + v); // _++... | _--...
                    }
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean v(String ind) {
        if (debug) System.out.println(ind + "v: checking " + ts.peek());
        if (ts.eof())
            return true;
        if (incrop(ind + sp)) {
            int ptr = stack.getPointer();
            if (v(ind + sp)) {
                String v = "";
                if (ptr < stack.getPointer()) {
                    if (debug) System.out.println(ind + "post-inc v");
                    v = " " + stack.pop();
                }
                String incrop = stack.pop();
                stack.push("_" + incrop + v);
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean d(String ind) {
        if (debug) System.out.println(ind + "d: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (is("$", ind + sp)) {
            if (b(ind + sp)) {
                if (debug) System.out.println(ind + "lvalue");
                String b = stack.pop(); // b
                stack.push(b + " " + stack.pop()); // $
                return true;
            }
            return false;
        }
        return e(ind + sp);
    }

    private boolean e(String ind) {
        if (debug) System.out.println(ind + "e: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (is("(", ind + sp)) {
            if (expr(ind + sp)) {
                if (is(")", ind + sp)) {
                    if (debug) System.out.println(ind + "parens");
                    stack.pop(); // )
                    String expr = stack.pop(); // expr
                    stack.pop(); // (
                    stack.push(expr);
                    return true;
                }
                return false;
            }
            return false;
        }
        return num(ind + sp);
    }

    private boolean incrop(String ind) {
        if (debug) System.out.println(ind + "incrop: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (ts.peek().equals("++") | ts.peek().equals("--")) {
            if (debug) System.out.println(ind + "consuming token");
            stack.push(ts.consume());
            return true;
        }
        return false;
    }

    private boolean binop(String ind) {
        if (debug) System.out.println(ind + "binop: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (ts.peek().equals("+") | ts.peek().equals("-")) {
            if (debug) System.out.println(ind + "consuming token");
            stack.push(ts.consume());
            return true;
        }
        return false;
    }

    private boolean num(String ind) {
        if (debug) System.out.println(ind + "num: checking " + ts.peek());
        if (ts.eof())
            return false;
        if (Pattern.matches("[0-9]", ts.peek())) {
            if (debug) System.out.println(ind + "consuming token");
            stack.push(ts.consume());
            return true;
        }
        return false;
    }

    private boolean is(String s, String ind) {
        if (debug) System.out.println(ind + "is: checking " + ts.peek() + " against " + s);
        if (ts.eof())
            return false;
        if (s.equals(ts.peek())) {
            if (debug) System.out.println(ind + "consuming token");
            stack.push(ts.consume());
            return true;
        }
        return false;
    }
}
