package elements;

public class EAssignmentSymbol extends ESymbol {
    private ESymbol accessor;
    private int offset;
    public EAssignmentSymbol(ESymbol accessor, int offset) {
        this.accessor = accessor;
        this.offset = offset;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return accessor.toVapor(tab, depth);
    }

    @Override
    public String toString() {
        return "[" + accessor + " + " + offset + "]";
    }
}
