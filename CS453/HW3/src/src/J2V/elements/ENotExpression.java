package J2V.elements;

public class ENotExpression extends EExpression {
    private EIf if_;
    private EExpression expr;
    public ENotExpression(EExpression expr) {
        super(new ETemporarySymbol());
        this.expr = expr;
        // conditional
        EEqualComparisonExpression cond = new EEqualComparisonExpression(
                expr, new EExpression(new EPrimitive(0)));
        if_ = new EIf(cond, null);
        if_.true_statements.add(new EAssignmentStatement(null,
                new EExpression(new EPrimitive(1)), getAccessor()));
        if_.false_statements.add(new EAssignmentStatement(null,
                new EExpression(new EPrimitive(0)), getAccessor()));
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = expr.toVapor(tab, depth);
        ret += if_.toVapor(tab, depth);
        return ret;
    }
}
