package elements;

public class EMinusExpression extends EExpression {
    private EExpression left;
    private EExpression right;
    public EMinusExpression(EExpression left, EExpression right) {
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
        ret += tab_ + getAccessor() + " = Sub(" + left.getAccessor() + " " + right.getAccessor() + ")\n";
        return ret;
    }
}
