package elements;

public class If implements Element {
    private int id;
    public ContainerElement true_statements = new ContainerElement();
    public ContainerElement false_statements = new ContainerElement();
    private String conditional;

    public If(int id, String conditional_variable) {
        this.id = id;
        conditional = conditional_variable;
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
        /**
         *   if0 t.0 goto :if1_else
         num_aux = 1
         goto :if1_end
         if1_else:
         t.1 = [this]
         t.1 = [t.1+0]
         t.2 = Sub(num 1)
         t.3 = call t.1(this t.2)
         num_aux = MulS(num t.3)
         if1_end:
         */
        // declare
        String ret = Element.repeatTab(tab, depth) +
                "if0 " + conditional + " goto " + elseLabel(-1) +
                "\n";
        // true statements
        ret += Element.repeatTab(tab, depth + 1) + true_statements.toVapor(tab, depth + 1);
        ret += "goto " + endLabel(-1);
        // false statements
        ret += Element.repeatTab(tab, depth) + elseLabel(1) + "\n";
        ret += false_statements.toVapor(tab, depth + 1);
        // convert all elements
        ret += Element.repeatTab(tab, depth) + endLabel(1);
        return ret + "\n";
    }
    // TODO implement
}
