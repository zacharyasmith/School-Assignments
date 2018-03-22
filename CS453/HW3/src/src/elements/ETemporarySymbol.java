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
        return "t." + id;
    }

    @Override
    public String toString() {
        return toVapor(null, 0);
    }
}
