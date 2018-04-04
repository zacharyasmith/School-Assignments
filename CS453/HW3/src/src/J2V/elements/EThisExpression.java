package J2V.elements;

public class EThisExpression extends EExpression {
    public EThisExpression() {
        super(new EThisSymbol());
    }

    @Override
    public String toVapor(String tab, int depth) {
        return "";
    }
}
