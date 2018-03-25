package elements;

public class EThisSymbol extends ESymbol {
    @Override
    public String toVapor(String tab, int depth) {
        return "";
    }

    @Override
    public String toString() {
        return "this";
    }
}
