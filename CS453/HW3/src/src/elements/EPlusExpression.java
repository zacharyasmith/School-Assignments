package elements;

public class EPlusExpression extends EExpression {
    private EExpression left;
    private EExpression right;
    public EPlusExpression(EExpression left, EExpression right) {
        super(new ETemporarySymbol());
        this.left = left;
        this.right = right;
    }

    @Override
    public String toVapor(String tab, int depth) {
        String ret = left.toVapor(tab, depth);
        ret += right.toVapor(tab, depth);
        String tab_ = Element.repeatTab(tab, depth);
        ret += tab_ + getAccessor() + " = Add(" + left.getAccessor() + " " + right.getAccessor() + ")\n";
        return ret;
    }
}
