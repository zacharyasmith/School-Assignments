package elements;

import context.Counters;

public class ETemporarySymbol extends ESymbol {
    private int id;

    public ETemporarySymbol() {
        id = Counters.nextSym();
    }

    public ETemporarySymbol(int id) {
        this.id = id;
    }

    @Override
    public String toVapor(String tab, int depth) {
        return "";
    }

    @Override
    public String toString() {
        return "t." + id;
    }
}
