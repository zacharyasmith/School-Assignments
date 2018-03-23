package elements;

import context.ContextObject;

public class EAssignmentStatement extends EStatement {
    private EExpression expr;
    private ESymbol tmp;

    public EAssignmentStatement (ContextObject c, EExpression expr, ESymbol tmp) {
        super(c);
        this.expr = expr;
        this.tmp = tmp;
    }

    @Override
    public String toVapor(String tab, int depth) {
        // TODO finish identifier computing
        String ret = expr.toVapor(tab, depth);
        ret += expr.getAccessor().toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + tmp + " = " + expr.getAccessor() + "\n";
        return ret;
    }
}
