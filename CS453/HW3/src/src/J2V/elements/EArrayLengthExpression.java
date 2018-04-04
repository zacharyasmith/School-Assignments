package J2V.elements;

public class EArrayLengthExpression extends EExpression {
    private EExpression accessor;
    public EArrayLengthExpression(EExpression accessor) {
        super(new ETemporarySymbol());
        this.accessor = accessor;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = accessor.toVapor(tab, depth);
        ret += accessor.getAccessor().toVapor(tab, depth);
        ret += Element.repeatTab(tab, depth) + getAccessor() + " = [" + accessor.getAccessor() + "]\n";
        return ret;
    }
}
