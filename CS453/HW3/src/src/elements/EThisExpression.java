package elements;

public class EThisExpression extends EExpression {
    @Override
    public String toString() {
        throw new RuntimeException("Should not be here.");
    }

    @Override
    public String toVapor(String tab, int depth) {
        throw new RuntimeException("Should not be here.");
    }
}
