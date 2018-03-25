package elements;

public class EThisExpression extends EExpression {
    @Override
    public String toString() {
        return "this";
    }

    @Override
    public String toVapor(String tab, int depth) {
        return "";
    }
}
