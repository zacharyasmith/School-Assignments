package elements;

import context.ClassObject;

public class EAccessorExpression extends EExpression {
    private ESymbol sym;
    private int offset;
    public ClassObject c;
    private boolean assignment = false;
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
        setAccessor(new EAssignmentSymbol(sym, offset));
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = "";
        if (!assignment) {
            ret += sym.toVapor(tab, depth);
            ret += Element.repeatTab(tab, depth) + getAccessor() + " = [" + sym + " + " + offset + "]\n";
        }
        return ret;
    }
}
