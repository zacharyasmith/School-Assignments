package elements;

import context.Counters;

public class EWhile implements Element {
    private int id = Counters.nextWhile();
    public EContainer statements = new EContainer();
    private EBooleanExpression expr;

    public EWhile(EBooleanExpression expr) {
        this.expr = expr;
    }

    @Override
    public String toVapor(String tab, int depth) {
        // declare
        String ret = Element.repeatTab(tab, depth) + "while" + id + "_test:\n";
        ret += expr.toVapor(tab, depth + 1);
        ret += Element.repeatTab(tab, depth + 1) + "if0 " +
                expr.getAssignment() + " goto :while" + id + "_end\n";
        ret += Element.repeatTab(tab, depth) + "while" + id + "_body:\n";
        ret += statements.toVapor(tab, depth + 1);
        ret += Element.repeatTab(tab, depth + 1) + "goto :while" + id + "_test";
        ret += Element.repeatTab(tab, depth) + "while" + id + "_end:\n";
        return ret;
    }
}
