package elements;

public class EAccessorExpression extends EExpression {
    private ESymbol sym;
    private int offset;
    /**
     * @param offset int bytes
     */
    public EAccessorExpression(ESymbol sym, int offset) {
        super(new ETemporarySymbol());
        this.sym = sym;
        this.offset = offset;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = "";
        ret += sym.toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + getAccessor() + " = [" + sym + " + " + offset + "]\n";
        return ret;
    }
}
