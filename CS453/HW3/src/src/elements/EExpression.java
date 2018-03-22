package elements;

public class EExpression implements Element {
    private ESymbol assignment;

    public String assnString() {
        return getAssignment().toVapor(null, 0);
    }

    public ESymbol getAssignment() {
        if (assignment == null)
            assignment = new ESymbol();
        return assignment;
    }

    public void setAssignment(ESymbol s) {
        assignment = s;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return null;
    }
}
