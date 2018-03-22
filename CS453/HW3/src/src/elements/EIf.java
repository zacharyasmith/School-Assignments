package elements;

import context.Counters;

public class EIf implements Element {
    private int id = Counters.nextIf();
    public EContainer true_statements = new EContainer();
    public EContainer false_statements = new EContainer();
    private ESymbol conditional = new ESymbol();

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
        String ret = Element.repeatTab(tab, depth) +
                "if0 " + conditional.toVapor(tab, 0) + " goto " + elseLabel(-1) +
                "\n";
        ret += true_statements.toVapor(tab, depth + 1);
        ret += "goto " + endLabel(-1);
        ret += Element.repeatTab(tab, depth) + elseLabel(1) + "\n";
        ret += false_statements.toVapor(tab, depth + 1);
        ret += Element.repeatTab(tab, depth) + endLabel(1) + "\n";
        return ret;
    }
}
