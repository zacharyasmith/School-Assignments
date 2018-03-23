package elements;

public class EThisSymbol extends ESymbol {
    @Override
    public String toVapor(String tab, int depth) {
        return "this";
    }

    @Override
    public String toString() {
        return toVapor(null, 0);
    }
}
