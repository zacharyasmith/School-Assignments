package elements;

public class EPrimitive extends ESymbol {
    private int val;

    public EPrimitive(int val) {
        this.val = val;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return "";
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
}
