package J2V.elements;

public class EArrayLookupExpression extends EExpression{
    private EExpression accessor;
    private EExpression index_expr;
    public EArrayLookupExpression(EExpression accessor, EExpression offset) {
        super(new ETemporarySymbol());
        this.accessor = accessor;
        this.index_expr = offset;
    }

    @Override
    public String toVapor(String tab, int depth) {
        // add 1, multiply 4
        ESymbol offset = new ETemporarySymbol();
        String ret = index_expr.getAccessor().toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + offset + " = call :ArrayIndexHelper(" +
                index_expr.getAccessor() + ")\n";
        EPlusExpression final_offset = new EPlusExpression(accessor, new EExpression(offset));
        ret += final_offset.toVapor(tab, depth);
        ret += final_offset.getAccessor().toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + getAccessor() +
                " = [" + final_offset.getAccessor() + "]\n";
        return ret;
    }
}
