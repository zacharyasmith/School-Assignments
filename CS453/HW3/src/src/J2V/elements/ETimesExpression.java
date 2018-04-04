package J2V.elements;

public class ETimesExpression extends EExpression {
    private EExpression left;
    private EExpression right;
    public ETimesExpression(EExpression left, EExpression right) {
        super(new ETemporarySymbol());
        this.left = left;
        this.right = right;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = left.toVapor(tab, depth);
        ret += right.toVapor(tab, depth);
        String tab_ = Element.repeatTab(tab, depth);
        ret += getAccessor().toVapor(tab, depth) + left.getAccessor().toVapor(tab, depth) +
                right.getAccessor().toVapor(tab, depth);
        ret += tab_ + getAccessor() + " = MulS(" + left.getAccessor() + " " + right.getAccessor() + ")\n";
        return ret;
    }
}
