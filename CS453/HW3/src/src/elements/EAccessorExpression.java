package elements;

import context.ClassObject;

public class EAccessorExpression extends EExpression {
    private ESymbol sym;
    private int offset;
    public ClassObject c;
    public boolean assignment = false;
    /**
     * @param offset int bytes
     */
    public EAccessorExpression(ESymbol sym, int offset) {
        super(new ETemporarySymbol());
        this.sym = sym;
        this.offset = offset;
    }

    public void setAssignment() {
        assignment = true;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = "";
        ret += sym.toVapor(tab, depth);
        if (!assignment)
            ret += Element.repeatTab(tab, depth) + getAccessor() + " = [" + sym + " + " + offset + "]\n";
        // else sym should output [sym + offset] = ...
        return ret;
    }
}
