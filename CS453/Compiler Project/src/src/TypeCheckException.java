import syntaxtree.*;

import static java.lang.System.exit;

public class TypeCheckException extends Exception {
    public TypeCheckException(String message) {
        super("TypeCheckException:: " + message);
    }

    public static void debugString(TypeCheckException e, ContextObject c, NodeToken n) {
        String message = e.getMessage() + "\n\t" +
                c + "\n\t" +
                "Row: " + n.beginLine + " Col: " + n.beginColumn;
        err(message, e);
    }

    /**
     * f0 -> IntegerLiteral()
     *       | TrueLiteral()
     *       | FalseLiteral()
     *       | Identifier()
     *       | ThisExpression()
     *       | ArrayAllocationExpression()
     *       | AllocationExpression()
     *       | NotExpression()
     *       | BracketExpression()
     */
    public static void debugString(TypeCheckException e, ContextObject c, PrimaryExpression p) {
        NodeToken n;
        switch (p.f0.which) {
            case 0:
                n = ((IntegerLiteral) p.f0.choice).f0;
                break;
            case 1:
                n = ((TrueLiteral) p.f0.choice).f0;
                break;
            case 2:
                n = ((FalseLiteral) p.f0.choice).f0;
                break;
            case 3:
                n = ((Identifier) p.f0.choice).f0;
                break;
            case 4:
                n = ((ThisExpression) p.f0.choice).f0;
                break;
            case 5:
                n = ((ArrayAllocationExpression) p.f0.choice).f0;
                break;
            case 6:
                n = ((AllocationExpression) p.f0.choice).f0;
                break;
            case 7:
                n = ((NotExpression) p.f0.choice).f0;
                break;
            default:
                n = ((BracketExpression) p.f0.choice).f0;
                break;
        }
        debugString(e, c, n);
    }

    /**
     * f0 -> AndExpression()
     *       | CompareExpression()
     *       | PlusExpression()
     *       | MinusExpression()
     *       | TimesExpression()
     *       | ArrayLookup()
     *       | ArrayLength()
     *       | MessageSend()
     *       | PrimaryExpression()
     */
    public static void debugString(TypeCheckException e, ContextObject c, Expression p) {
        NodeToken n = null;
        switch (p.f0.which) {
            case 0:
                n = ((AndExpression) p.f0.choice).f1;
                break;
            case 1:
                n = ((CompareExpression) p.f0.choice).f1;
                break;
            case 2:
                n = ((PlusExpression) p.f0.choice).f1;
                break;
            case 3:
                n = ((MinusExpression) p.f0.choice).f1;
                break;
            case 4:
                n = ((TimesExpression) p.f0.choice).f1;
                break;
            case 5:
                n = ((ArrayLookup) p.f0.choice).f1;
                break;
            case 6:
                n = ((ArrayLength) p.f0.choice).f1;
                break;
            case 7:
                n = ((MessageSend) p.f0.choice).f1;
                break;
            default:
                debugString(e, c, (PrimaryExpression) p.f0.choice);
        }
        debugString(e, c, n);
    }

    private static void err(String m, TypeCheckException e) {
        System.err.println(m);
        e.printStackTrace(System.err);
    }
}
