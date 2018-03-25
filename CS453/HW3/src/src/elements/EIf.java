package elements;

import context.ContextObject;
import context.Counters;

public class EIf implements Element {
    private int id = Counters.nextIf();
    public EContainer<EStatement> true_statements;
    public EContainer<EStatement> false_statements;
    private EExpression conditional;

    public EIf(EExpression conditional, ContextObject c) {
        this.conditional = conditional;
        true_statements = new EContainer<>(c);
        false_statements = new EContainer<>(c);
    }

    private String elseLabel (int colon) {
        return (colon < 0 ? ":" : "") +
                "if" + id + "_else" +
                (colon > 0 ? ":" : "");
    }

    private String endLabel (int colon) {
        return (colon < 0 ? ":" : "") +
                "if" + id + "_end" +
                (colon > 0 ? ":" : "");
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = conditional.toVapor(tab, depth);
        ret += conditional.getAccessor().toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) +
                "if0 " + conditional.getAccessor() + " goto " + elseLabel(-1) +
                "\n";
        ret += true_statements.toVapor(tab, depth + 1);
        ret += Element.repeatTab(tab, depth) + "goto " + endLabel(-1) + "\n";
        ret += Element.repeatTab(tab, depth) + elseLabel(1) + "\n";
        ret += false_statements.toVapor(tab, depth + 1);
        ret += Element.repeatTab(tab, depth) + endLabel(1) + "\n";
        return ret;
    }
}
