package elements;

public class EArrayAssignmentStatement extends EStatement {
    private EExpression index_expr;
    private EExpression assn_expr;
    private ESymbol arr;
    public EArrayAssignmentStatement(EExpression index_expr, EExpression assn_expr, ESymbol arr) {
        super(null);
        this.index_expr = index_expr;
        this.assn_expr = assn_expr;
        this.arr = arr;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = index_expr.toVapor(tab, depth);
        // create temp for offset
        ESymbol offset = new ETemporarySymbol();
        // add 1, multiply 4
        ret += Element.repeatTab(tab, depth) + offset + " = call :ArrayIndexHelper(" +
                index_expr.getAccessor() + ")\n";
        // add array location with offset
        ret += Element.repeatTab(tab, depth) + offset + " = Add(" +
                arr + " " + offset + ")\n";
        // compute assignment
        ret += assn_expr.toVapor(tab, depth);
        // print statement
        ret += Element.repeatTab(tab, depth) + "[" + offset + "] = " +
                assn_expr.getAccessor() + "\n";
        return ret;
    }
}
