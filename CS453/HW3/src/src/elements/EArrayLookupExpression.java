package elements;

public class EArrayLookupExpression extends EExpression{
    private EExpression accessor;
    private EExpression offset;
    public EArrayLookupExpression(EExpression accessor, EExpression offset) {
        super(new ETemporarySymbol());
        this.accessor = accessor;
        this.offset = new EPlusExpression(offset, new EExpression(new EPrimitive(1)));
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = accessor.toVapor(tab, depth);
        ret += accessor.getAccessor().toVapor(tab, depth);
        ret += offset.toVapor(tab, depth);
        ret += offset.getAccessor().toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + getAccessor() +
                " = [" + accessor.getAccessor() + " + " + offset.getAccessor() + "]\n";
        return ret;
    }
}
