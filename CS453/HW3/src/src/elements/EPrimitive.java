package elements;

public class EPrimitive extends ESymbol {
    private int val;

    public EPrimitive(int val) {
        this.val = val;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return String.valueOf(val);
    }

    @Override
    public String toString() {
        return toVapor(null, 0);
    }
}
