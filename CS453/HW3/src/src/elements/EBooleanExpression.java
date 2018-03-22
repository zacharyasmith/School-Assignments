package elements;

public class EBooleanExpression extends EExpression {
    EExpression left;
    EExpression right;

    public EBooleanExpression(EExpression left, EExpression right) {
        this.left = left;
        this.right = right;
    }
}
