package J2V.elements;

public class EArrayAllocationExpression extends EExpression {
    private EExpression index_expression;

    public EArrayAllocationExpression(EExpression index_expression) {
        super(new ETemporarySymbol());
        this.index_expression = index_expression;
    }

    @Override
    public String toVapor(String tab, int depth) {
        // t.0=call :AllocArray(10)
        String ret = index_expression.toVapor(tab, depth);
        // compute accessor separately if necessary
        ret += getAccessor().toVapor(tab, depth);
        ret += index_expression.getAccessor().toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + getAccessor() + " = call :AllocArray(" +
                index_expression.getAccessor() + ")\n";
        return ret;
    }
}
