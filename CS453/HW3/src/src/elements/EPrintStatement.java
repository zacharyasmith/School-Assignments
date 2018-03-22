package elements;

public class EPrintStatement extends EStatement {
    private EExpression expr;

    public EPrintStatement(EExpression expr) {
        super(null);
        this.expr = expr;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = expr.toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + "PrintIntS(" + expr.getAccessor() + ")\n";
        return ret;
    }
}
