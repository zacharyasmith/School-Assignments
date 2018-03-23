package elements;

public class EAccessorSymbol extends ESymbol {
    private int offset;

    public EAccessorSymbol(int offset) {
        this.offset = offset;
    }

    @Override
    public String toVapor(String tab, int depth) {
        // TODO FIX
        return "[!!! + " + offset + "]";
    }

    @Override
    public String toString() {
        return toVapor(null, 0);
    }
}
