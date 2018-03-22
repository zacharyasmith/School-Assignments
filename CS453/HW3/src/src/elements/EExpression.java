package elements;

public class EExpression implements Element {
    private ESymbol accessor;

    public EExpression() {
    }

    public EExpression(ESymbol accessor) {
        this.accessor = accessor;
    }

    public ESymbol getAccessor() {
        if (accessor == null)
            accessor = new ETemporarySymbol();
        return accessor;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return "";
    }
}
