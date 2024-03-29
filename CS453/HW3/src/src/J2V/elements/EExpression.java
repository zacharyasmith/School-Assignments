package J2V.elements;

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

    public void setAccessor(ESymbol accessor) {
        this.accessor = accessor;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return "";
    }
}
