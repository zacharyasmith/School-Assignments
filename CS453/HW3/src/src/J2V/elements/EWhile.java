package J2V.elements;

import J2V.context.ContextObject;
import J2V.context.Counters;

public class EWhile implements Element {
    private int id = Counters.nextWhile();
    public EContainer<EStatement> statements;
    private EExpression expr;

    public EWhile(ContextObject c, EExpression expr) {
        this.expr = expr;
        statements = new EContainer<>(c);
    }

    @Override
    public String toVapor(String tab, int depth) {
        // declare
        String ret = Element.repeatTab(tab, depth) + "while" + id + "_test:\n";
        ret += expr.toVapor(tab, depth + 1);
        ret += expr.getAccessor().toVapor(tab, depth + 1);
        ret += Element.repeatTab(tab, depth + 1) + "if0 " +
                expr.getAccessor() + " goto :while" + id + "_end\n";
        ret += Element.repeatTab(tab, depth) + "while" + id + "_body:\n";
        ret += statements.toVapor(tab, depth + 1);
        ret += Element.repeatTab(tab, depth + 1) + "goto :while" + id + "_test\n";
        ret += Element.repeatTab(tab, depth) + "while" + id + "_end:\n";
        return ret;
    }
}
