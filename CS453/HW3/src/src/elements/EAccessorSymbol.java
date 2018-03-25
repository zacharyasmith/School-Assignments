package elements;

public class EAccessorSymbol extends ESymbol {
    private int offset;
    private ETemporarySymbol s = new ETemporarySymbol();

    public EAccessorSymbol(int offset) {
        this.offset = offset;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return Element.repeatTab(tab, depth) + s + " = [this + " + offset + "]\n";
    }

    @Override
    public String toString() {
        return s.toString();
    }
}
